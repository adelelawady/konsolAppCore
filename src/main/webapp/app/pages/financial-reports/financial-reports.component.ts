import { Component, OnInit, AfterViewInit, ElementRef, ViewChild, OnDestroy, ChangeDetectorRef } from '@angular/core';
import { FinancialReportsService } from 'app/core/konsolApi/api/financialReports.service';
import { FinancialDashboardDTO } from 'app/core/konsolApi/model/financialDashboardDTO';
import { FinancialSearchDTO } from 'app/core/konsolApi/model/financialSearchDTO';
import * as echarts from 'echarts';
import { EChartsCoreOption, EChartsOption } from 'echarts';
import { ECBasicOption } from 'echarts/types/dist/shared';

@Component({
  selector: 'jhi-financial-reports',
  templateUrl: './financial-reports.component.html',
  styleUrls: ['./financial-reports.component.scss'],
})
export class FinancialReportsComponent implements OnInit, AfterViewInit, OnDestroy {
  @ViewChild('dailySalesChart', { static: true }) dailySalesChart!: ElementRef;
  @ViewChild('salesVsCostsChart', { static: true }) salesVsCostsChart!: ElementRef;
  @ViewChild('profitMarginChart', { static: true }) profitMarginChart!: ElementRef;
  @ViewChild('topItemsChart', { static: true }) topItemsChart!: ElementRef;
  @ViewChild('cashFlowTrendChart', { static: true }) cashFlowTrendChart!: ElementRef;
  @ViewChild('bankBalanceChart', { static: true }) bankBalanceChart!: ElementRef;
  @ViewChild('topItemsByQuantityChart', { static: true }) topItemsByQuantityChart!: ElementRef;
  @ViewChild('performanceRatiosChart', { static: true }) performanceRatiosChart!: ElementRef;
  @ViewChild('marginTrendsChart', { static: true }) marginTrendsChart!: ElementRef;
  @ViewChild('profitBreakdownChart', { static: true }) profitBreakdownChart!: ElementRef;

  options: EChartsOption = {};
  dashboardData?: any;
  salesMetrics: any;
  performanceIndicators: any;
  storeMetrics: any;
  topSellingItems: any[] = [];
  cashFlowMetrics: any;
  dailySalesTrendOption: EChartsOption = {};
  salesVsCostsOption: EChartsOption = {};
  profitMarginDistributionOption: EChartsOption = {};
  topItemsByRevenueOption: EChartsOption = {};
  topItemsByQuantityOption: EChartsOption = {};

  performanceRatiosOption: EChartsOption = {};
  marginTrendsOption: EChartsOption = {};
  profitBreakdownOption: EChartsOption = {};

  cashFlowTrendOption: any;
  bankBalanceOption: any;
  optionsX: EChartsOption = {};

  loading = true;
  selectedDateRange = 'week';
  dateRange = {
    startDate: new Date(),
    endDate: new Date(),
  };

  private charts: { [key: string]: echarts.ECharts | undefined } = {
    dailySales: undefined,
    salesVsCosts: undefined,
    profitMargin: undefined,
    topItems: undefined,
    cashFlowTrend: undefined,
    bankBalance: undefined,
    performanceRatios: undefined,
    marginTrends: undefined,
    profitBreakdown: undefined,
    topItemsByQuantity: undefined,
  };

  constructor(private financialReportsService: FinancialReportsService, private cdr: ChangeDetectorRef) {}

  ngOnDestroy(): void {
    // Dispose of all chart instances
    Object.values(this.charts).forEach(chart => {
      if (chart) {
        chart.dispose();
      }
    });

    // Remove resize listener
    window.removeEventListener('resize', this.handleResize);
  }

  ngOnInit(): void {
    const xAxisData = [];
    const data1 = [];
    const data2 = [];

    for (let i = 0; i < 100; i++) {
      xAxisData.push('category' + i);
      data1.push((Math.sin(i / 5) * (i / 5 - 10) + i / 6) * 5);
      data2.push((Math.cos(i / 5) * (i / 5 - 10) + i / 6) * 5);
    }

    this.optionsX = {
      legend: {
        data: ['bar', 'bar2'],
        align: 'left',
      },
      tooltip: {},
      xAxis: {
        data: xAxisData,
        silent: false,
        splitLine: {
          show: false,
        },
      },
      yAxis: {},
      series: [
        {
          name: 'bar',
          type: 'bar',
          data: data1,
          animationDelay: idx => idx * 10,
        },
        {
          name: 'bar2',
          type: 'bar',
          data: data2,
          animationDelay: idx => idx * 10 + 100,
        },
      ],
      animationEasing: 'elasticOut',
      animationDelayUpdate: idx => idx * 5,
    };

    this.initializeChartOptions();
  }

  ngAfterViewInit(): void {
    // Initialize charts after view is ready
    setTimeout(() => {
      this.initializeCharts();
      this.setDateRange({ target: { value: 'week' } });

      // Add resize event listener
      window.addEventListener('resize', this.handleResize);
    });
  }

  private handleResize = () => {
    Object.values(this.charts).forEach(chart => {
      if (chart) {
        chart.resize();
      }
    });
  };

  private initializeCharts(): void {
    if (this.dailySalesChart) {
      this.charts.dailySales = echarts.init(this.dailySalesChart.nativeElement);
    }
    if (this.salesVsCostsChart) {
      this.charts.salesVsCosts = echarts.init(this.salesVsCostsChart.nativeElement);
    }
    if (this.profitMarginChart) {
      this.charts.profitMargin = echarts.init(this.profitMarginChart.nativeElement);
    }
    if (this.topItemsChart) {
      this.charts.topItems = echarts.init(this.topItemsChart.nativeElement);
    }
    if (this.cashFlowTrendChart) {
      this.charts.cashFlowTrend = echarts.init(this.cashFlowTrendChart.nativeElement);
    }
    if (this.bankBalanceChart) {
      this.charts.bankBalance = echarts.init(this.bankBalanceChart.nativeElement);
    }
  }

  private initializeChartOptions(): void {
    // Set default options for all charts
    this.dailySalesTrendOption = this.getDefaultChartOptions('Daily Sales Trend');
    this.salesVsCostsOption = this.getDefaultChartOptions('Sales vs Costs');
    this.profitMarginDistributionOption = this.getDefaultChartOptions('Profit Margin Distribution');
    this.topItemsByRevenueOption = this.getDefaultChartOptions('Top Items by Revenue');
  }

  private getDefaultChartOptions(title: string): EChartsOption {
    return {
      title: {
        text: title,
        left: 'center',
      },
      tooltip: {
        trigger: 'axis',
      },
      grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true,
      },
    };
  }

  private updateChartOptions(): void {
    if (this.dashboardData) {
      // Update metrics
      this.updateMetrics();

      // Update charts
      this.updateDailySalesTrend();
      this.updateSalesVsCosts();
      this.updateProfitMarginDistribution();
      this.updateTopItemsByRevenue();
      this.updateCashFlowTrend();
      this.updateBankBalance();
      this.updateTopItemsByQuantity();
      this.updatePerformanceCharts();
      // Update chart instances with new options
      if (this.charts.dailySales) {
        this.charts.dailySales.setOption(this.dailySalesTrendOption, true);
      }
      if (this.charts.salesVsCosts) {
        this.charts.salesVsCosts.setOption(this.salesVsCostsOption, true);
      }
      if (this.charts.profitMargin) {
        this.charts.profitMargin.setOption(this.profitMarginDistributionOption, true);
      }
      if (this.charts.topItems) {
        this.charts.topItems.setOption(this.topItemsByRevenueOption, true);
      }
      if (this.charts.cashFlowTrend) {
        this.charts.cashFlowTrend.setOption(this.cashFlowTrendOption, true);
      }
      if (this.charts.bankBalance) {
        this.charts.bankBalance.setOption(this.bankBalanceOption, true);
      }

      if (this.charts.topItemsByQuantity) {
        this.charts.topItemsByQuantity.setOption(this.topItemsByQuantityOption, true);
      }

      // Add these chart instance updates
      if (this.charts.performanceRatios) {
        this.charts.performanceRatios.setOption(this.performanceRatiosOption, true);
      }
      if (this.charts.marginTrends) {
        this.charts.marginTrends.setOption(this.marginTrendsOption, true);
      }
      if (this.charts.profitBreakdown) {
        this.charts.profitBreakdown.setOption(this.profitBreakdownOption, true);
      }

      this.cdr.detectChanges();
    }
  }

  private updateMetrics(): void {
    if (this.dashboardData) {
      // Update sales metrics
      this.salesMetrics = {
        totalSales: this.dashboardData.totalSales || 0,
        netProfit: this.dashboardData.netProfit || 0,
      };

      // Update performance indicators
      this.performanceIndicators = {
        netProfitMargin: (this.dashboardData.netProfit / this.dashboardData.totalSales) * 100 || 0,
        currentRatio: this.dashboardData.currentRatio || 0,
      };

      // Update store metrics
      this.storeMetrics = {
        totalRevenue: this.dashboardData.totalRevenue || 0,
        transactionCount: this.dashboardData.transactionCount || 0,
        averageTransactionValue: this.dashboardData.averageTransactionValue || 0,
        profitMargin: this.dashboardData.profitMargin || 0,
      };

      // Update top selling items
      if (this.dashboardData?.invoiceItemAnalysis) {
        const analysis = this.dashboardData.invoiceItemAnalysis;

        // Update sales metrics
        this.salesMetrics = {
          totalSales: analysis.topSellingItemRevenue || 0,
          netProfit: analysis.itemProfitMargins?.[0]?.grossProfit || 0,
        };

        // Update performance indicators
        this.performanceIndicators = {
          netProfitMargin: analysis.itemProfitMargins?.[0]?.profitMargin || 0,
          currentRatio: analysis.itemProfitMargins?.[0]?.averagePrice || 0,
        };

        // Update top selling items directly from the data
        this.topSellingItems = analysis.topSellingItems || [];
      }

      // Update cash flow metrics
      this.cashFlowMetrics = {
        totalMoneyIn: this.dashboardData.totalMoneyIn || 0,
        totalMoneyOut: this.dashboardData.totalMoneyOut || 0,
        currentCashPosition: this.dashboardData.currentCashPosition || 0,
      };
    }
  }

  private updateDailySalesTrend(): void {
    const salesData: any = this.dashboardData?.salesCharts?.find((chart: { chartType: string }) => chart.chartType === 'line');
    if (salesData) {
      this.dailySalesTrendOption = {
        ...this.getDefaultChartOptions('Daily Sales Trend'),
        tooltip: {
          trigger: 'axis',
          formatter: (params: any) => {
            const data = Array.isArray(params) ? params[0] : params;
            return `${data.name}<br/>${data.seriesName}: ₺${data.value}`;
          },
        },
        xAxis: {
          type: 'category',
          data: salesData.labels || [],
          axisLabel: {
            rotate: 30,
            formatter: (value: string) => {
              const date = new Date(value);
              return `${date.getDate()}/${date.getMonth() + 1}`;
            },
          },
        },
        yAxis: {
          type: 'value',
          name: 'Revenue (₺)',
          axisLabel: {
            formatter: '₺{value}',
          },
        },
        series:
          salesData.series?.map((series: { name: any; type: any; data: any }) => ({
            name: series.name,
            type: series.type || 'line',
            data: series.data,
            smooth: true,
            areaStyle: {},
          })) || [],
      };
    }
  }

  private updateSalesVsCosts(): void {
    const salesVsCostsData: any = this.dashboardData?.salesCharts?.find((chart: any) => chart.title.includes('Sales vs Costs'));
    if (salesVsCostsData) {
      this.salesVsCostsOption = {
        ...this.getDefaultChartOptions('Sales vs Costs'),
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow',
          },
          formatter: (params: any) => {
            return params.map((param: any) => `${param.seriesName}: ₺${param.value}`).join('<br/>');
          },
        },
        legend: {
          data: ['Sales', 'Costs'],
          top: '30px',
        },
        xAxis: {
          type: 'category',
          data: salesVsCostsData.labels || [],
          axisLabel: {
            rotate: 30,
          },
        },
        yAxis: {
          type: 'value',
          name: 'Amount (₺)',
          axisLabel: {
            formatter: '₺{value}',
          },
        },
        series:
          salesVsCostsData.series?.map((series: any) => ({
            name: series.name,
            type: series.type || 'bar',
            data: series.data,
            emphasis: {
              focus: 'series',
            },
          })) || [],
      };
    }
  }

  private updateProfitMarginDistribution(): void {
    const profitData = this.dashboardData?.salesCharts?.find((chart: { chartType: string }) => chart.chartType === 'pie');
    if (profitData) {
      this.profitMarginDistributionOption = {
        ...this.getDefaultChartOptions('Profit Margin Distribution'),
        tooltip: {
          trigger: 'item',
          formatter: '{b}: {c}%',
        },
        legend: {
          orient: 'vertical',
          left: 'left',
        },
        series: [
          {
            type: 'pie',
            radius: ['40%', '70%'],
            avoidLabelOverlap: false,
            itemStyle: {
              borderRadius: 10,
              borderColor: '#fff',
              borderWidth: 2,
            },
            label: {
              show: true,
              formatter: '{b}: {c}%',
            },
            emphasis: {
              label: {
                show: true,
                fontSize: '20',
                fontWeight: 'bold',
              },
            },
            data:
              profitData.series?.[0]?.data?.map((value: number, index: number) => ({
                value,
                name: profitData.labels?.[index] || '',
              })) || [],
          },
        ],
      };
    }
  }
  /*
  private updateTopItemsByRevenue(): void {
    const topItemsData = this.dashboardData?.itemSalesCharts?.find((chart:any) => chart.title.includes('Top 10 Items by Revenue'));
    if (topItemsData) {
      this.topItemsByRevenueOption = {
        ...this.getDefaultChartOptions('Top Items by Revenue'),
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          },
          formatter: (params: any) => {
            const data = Array.isArray(params) ? params[0] : params;
            return `${data.name}<br/>Revenue: ₺${data.value}`;
          }
        },
        grid: {
          left: '15%',
          right: '10%'
        },
        xAxis: {
          type: 'value',
          name: 'Revenue (₺)',
          axisLabel: {
            formatter: '₺{value}'
          }
        },
        yAxis: {
          type: 'category',
          data: topItemsData.labels || [],
          inverse: true,
          axisLabel: {
            width: 120,
            overflow: 'truncate'
          }
        },
        series: [{
          type: 'bar',
          data: topItemsData.series?.[0]?.data || [],
          label: {
            show: true,
            position: 'right',
            formatter: '₺{c}'
          },
          itemStyle: {
            color: '#2f4554'
          }
        }]
      };
    }
  }
*/

  private updateTopItemsByRevenue(): void {
    const revenueData: any = this.dashboardData?.invoiceItemAnalysis?.itemSalesCharts?.find(
      (chart: any) => chart.title === 'Top 10 Items by Revenue'
    );
    if (revenueData) {
      this.topItemsByRevenueOption = {
        ...this.getDefaultChartOptions('Top Items by Revenue'),
        tooltip: {
          trigger: 'axis',
          formatter: revenueData.options?.tooltip?.formatter || '{b}<br/>Revenue: ₺{c}',
        },
        xAxis: {
          type: 'value',
          name: 'Revenue (₺)',
          axisLabel: {
            formatter: '₺{value}',
          },
        },
        yAxis: {
          type: 'category',
          data: revenueData.labels || [],
          name: revenueData.options?.yAxis?.[0]?.name || 'Items',
        },
        series: [
          {
            name: revenueData.series?.[0]?.name || 'Revenue',
            type: 'bar',
            data: revenueData.series?.[0]?.data || [],
            itemStyle: {
              color: revenueData.series?.[0]?.style?.color || '#2f4554',
            },
            label: {
              show: true,
              position: 'right',
              formatter: '₺{c}',
            },
          },
        ],
      };
    }
  }
  private updateCashFlowTrend(): void {
    const cashFlowData: any = this.dashboardData?.cashFlowCharts?.find((chart: any) => chart.chartType === 'line');
    if (cashFlowData) {
      this.cashFlowTrendOption = {
        ...this.getDefaultChartOptions('Cash Flow Trend'),
        tooltip: {
          trigger: 'axis',
          formatter: (params: any) => {
            return params.map((param: any) => `${param.seriesName}: ₺${param.value.toLocaleString()}`).join('<br/>');
          },
        },
        legend: {
          data: ['Money In', 'Money Out', 'Net Flow'],
        },
        xAxis: {
          type: 'category',
          data: cashFlowData.xAxisData || [],
          name: 'Date',
        },
        yAxis: {
          type: 'value',
          name: 'Amount (₺)',
          axisLabel: {
            formatter: '₺{value}',
          },
        },
        series: cashFlowData.series?.map((series: any) => ({
          name: series.name,
          type: 'line',
          data: series.data,
          smooth: true,
          areaStyle: {},
        })),
      };
    }
  }

  private updateBankBalance(): void {
    const balanceData: any = this.dashboardData?.bankBalanceCharts?.find((chart: any) => chart.chartType === 'bar');
    if (balanceData) {
      this.bankBalanceOption = {
        ...this.getDefaultChartOptions('Bank Balance'),
        tooltip: {
          trigger: 'axis',
          formatter: '{b}: ₺{c}',
        },
        xAxis: {
          type: 'category',
          data: balanceData.xAxisData || [],
          name: 'Account',
        },
        yAxis: {
          type: 'value',
          name: 'Balance (₺)',
          axisLabel: {
            formatter: '₺{value}',
          },
        },
        series: [
          {
            type: 'bar',
            data: balanceData.series?.[0]?.data || [],
            itemStyle: {
              color: '#91cc75',
            },
            label: {
              show: true,
              position: 'top',
              formatter: '₺{c}',
            },
          },
        ],
      };
    }
  }

  private updateTopItemsByQuantity(): void {
    const quantityData: any = this.dashboardData?.invoiceItemAnalysis?.itemSalesCharts?.find(
      (chart: any) => chart.title === 'Top 10 Items by Quantity'
    );
    if (quantityData) {
      this.topItemsByQuantityOption = {
        ...this.getDefaultChartOptions('Top Items by Quantity'),
        tooltip: {
          trigger: 'axis',
          formatter: quantityData.options?.tooltip?.formatter || '{b}<br/>Quantity: {c}',
        },
        xAxis: {
          type: 'value',
          name: 'Quantity',
        },
        yAxis: {
          type: 'category',
          data: quantityData.labels || [],
          name: quantityData.options?.yAxis?.[0]?.name || 'Items',
        },
        series: [
          {
            name: quantityData.series?.[0]?.name || 'Quantity',
            type: 'bar',
            data: quantityData.series?.[0]?.data || [],
            itemStyle: {
              color: quantityData.series?.[0]?.style?.color || '#61a0a8',
            },
            label: {
              show: true,
              position: 'right',
            },
          },
        ],
      };
    }
  }

  // Add these new methods
  private updatePerformanceCharts(): void {
    this.updatePerformanceRatios();
    this.updateMarginTrends();
    this.updateProfitBreakdown();
  }

  private updatePerformanceRatios(): void {
    const ratiosData = this.dashboardData?.performanceCharts?.find((chart: any) => chart.title === 'Performance Ratios');
    if (ratiosData) {
      this.performanceRatiosOption = {
        ...this.getDefaultChartOptions('Performance Ratios'),
        tooltip: {
          trigger: 'item',
        },
        radar: {
          indicator: ratiosData.labels.map((label: string) => ({
            name: label,
            max: 100,
          })),
        },
        series: [
          {
            type: 'radar',
            data: [
              {
                value: [
                  this.performanceIndicators.grossProfitMargin,
                  this.performanceIndicators.netProfitMargin,
                  100 - this.performanceIndicators.operatingExpensesRatio,
                  this.performanceIndicators.revenueGrowth,
                  this.performanceIndicators.currentRatio * 10,
                ],
                name: 'Performance Metrics',
              },
            ],
          },
        ],
      };
    }
  }

  private updateMarginTrends(): void {
    const trendsData = this.dashboardData?.performanceCharts?.find((chart: any) => chart.title === 'Margin Trends');
    if (trendsData) {
      this.marginTrendsOption = {
        ...this.getDefaultChartOptions('Margin Trends'),
        tooltip: {
          trigger: 'axis',
        },
        xAxis: {
          type: 'category',
          data: trendsData.labels || [],
        },
        yAxis: {
          type: 'value',
          name: 'Margin %',
          axisLabel: {
            formatter: '{value}%',
          },
        },
        series: [
          {
            name: 'Margin',
            type: 'line',
            data: trendsData.series?.[0]?.data || [],
            smooth: true,
            areaStyle: {},
          },
        ],
      };
    }
  }

  private updateProfitBreakdown(): void {
    const profitData = this.dashboardData?.performanceCharts?.find((chart: any) => chart.title === 'Profit Breakdown');
    if (profitData) {
      this.profitBreakdownOption = {
        ...this.getDefaultChartOptions('Profit Breakdown'),
        tooltip: {
          trigger: 'item',
          formatter: '{b}: {c}%',
        },
        series: [
          {
            name: 'Profit Breakdown',
            type: 'pie',
            radius: ['50%', '70%'],
            avoidLabelOverlap: false,
            label: {
              show: true,
              formatter: '{b}: {c}%',
            },
            emphasis: {
              label: {
                show: true,
                fontSize: '16',
                fontWeight: 'bold',
              },
            },
            data: [
              { value: this.performanceIndicators.grossProfitMargin, name: 'Gross Profit' },
              { value: this.performanceIndicators.operatingExpensesRatio, name: 'Operating Expenses' },
              { value: this.performanceIndicators.netProfitMargin, name: 'Net Profit' },
            ],
          },
        ],
      };
    }
  }

  setDateRange(range: any): void {
    this.selectedDateRange = range.target.value;
    const now = new Date();
    const today = new Date(now.getFullYear(), now.getMonth(), now.getDate());

    switch (this.selectedDateRange) {
      case 'today':
        this.dateRange = {
          startDate: today,
          endDate: now,
        };
        break;
      case 'week':
        const weekStart = new Date(today);
        weekStart.setDate(today.getDate() - today.getDay());
        this.dateRange = {
          startDate: weekStart,
          endDate: now,
        };
        break;
      case 'month':
        const monthStart = new Date(today.getFullYear(), today.getMonth(), 1);
        this.dateRange = {
          startDate: monthStart,
          endDate: now,
        };
        break;
      case 'year':
        const yearStart = new Date(today.getFullYear(), 0, 1);
        this.dateRange = {
          startDate: yearStart,
          endDate: now,
        };
        break;
    }

    this.loadDashboardData();
  }

  loadDashboardData(): void {
    this.loading = true;

    const searchDTO: FinancialSearchDTO = {
      startDate: this.dateRange.startDate.toISOString(),
      endDate: this.dateRange.endDate.toISOString(),
      bankId: '674ffcaab0c83957e9623138',
      accountId: '675068e4e31429211961ed67',
      storeId: '674ffcaab0c83957e9623139',
    };

    this.financialReportsService.getFinancialDashboard(searchDTO).subscribe({
      next: (data: FinancialDashboardDTO | undefined) => {
        if (data) {
          this.dashboardData = data;
          this.updateChartOptions();
        }
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (error: any) => {
        console.error('Error loading dashboard data:', error);
        this.loading = false;
        this.cdr.detectChanges();
      },
    });
  }
}
