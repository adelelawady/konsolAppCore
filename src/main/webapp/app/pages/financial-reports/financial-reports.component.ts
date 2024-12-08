import { Component, OnInit, AfterViewInit, ElementRef, ViewChild, OnDestroy, ChangeDetectorRef } from '@angular/core';
import { FinancialReportsService } from 'app/core/konsolApi/api/financialReports.service';
import { FinancialDashboardDTO } from 'app/core/konsolApi/model/financialDashboardDTO';
import { FinancialSearchDTO } from 'app/core/konsolApi/model/financialSearchDTO';
import * as echarts from 'echarts';
import { EChartsCoreOption, EChartsOption } from 'echarts';
import { ECBasicOption } from 'echarts/types/dist/shared';
import { TranslateService } from '@ngx-translate/core';

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

  selectedAccount: any = null;
  selectedBank: any = null;
  selectedStore: any = null;

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

  cashFlowTrendOption: EChartsOption = {};
  bankBalanceOption: EChartsOption = {};
  optionsX: EChartsOption = {};

  loading = false;
  searchTerm = '';
  selectedPeriod = 'monthly';
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

  storeAccountAnalysis: any;
  itemAnalysis: any;
  invoiceAnalysis: any;

  constructor(
    private financialReportsService: FinancialReportsService,
    private cdr: ChangeDetectorRef,
    private translateService: TranslateService
  ) {}

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
    this.dailySalesTrendOption = this.getDefaultChartOptions('financialReports.charts.dailySales');
    this.salesVsCostsOption = this.getDefaultChartOptions('financialReports.charts.salesVsCosts');
    this.profitMarginDistributionOption = this.getDefaultChartOptions('financialReports.charts.profitMargin');
    this.topItemsByRevenueOption = this.getDefaultChartOptions('financialReports.charts.topItemsByRevenue');
    this.topItemsByQuantityOption = this.getDefaultChartOptions('financialReports.charts.topItemsByQuantity');
    this.cashFlowTrendOption = this.getDefaultChartOptions('financialReports.charts.cashFlowTrend');
    this.bankBalanceOption = this.getDefaultChartOptions('financialReports.charts.bankBalance.title');
    this.performanceRatiosOption = this.getDefaultChartOptions('financialReports.charts.performanceRatios.title');
    this.marginTrendsOption = this.getDefaultChartOptions('financialReports.charts.marginTrends.title');
    this.profitBreakdownOption = this.getDefaultChartOptions('financialReports.charts.profitBreakdown.title');
  }

  private getDefaultChartOptions(titleKey: string): EChartsOption {
    return {
      title: {
        text: this.translateService.instant(titleKey),
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
        totalSales: this.dashboardData.salesMetrics?.totalSales || 0,
        netSales: this.dashboardData.salesMetrics?.netSales || 0,
        totalCost: this.dashboardData.salesMetrics?.totalCost || 0,
        netCost: this.dashboardData.salesMetrics?.netCost || 0,
        netProfit: this.dashboardData.salesMetrics?.netProfit || 0,
        dailyRevenue: this.dashboardData.salesMetrics?.dailyRevenue || 0,
        monthlyRevenue: this.dashboardData.salesMetrics?.monthlyRevenue || 0,
      };

      // Update performance indicators
      this.performanceIndicators = {
        grossProfitMargin: this.dashboardData.performanceIndicators?.grossProfitMargin || 0,
        netProfitMargin: this.dashboardData.performanceIndicators?.netProfitMargin || 0,
        operatingExpensesRatio: this.dashboardData.performanceIndicators?.operatingExpensesRatio || 0,
        currentRatio: this.dashboardData.performanceIndicators?.currentRatio || 0,
        quickRatio: this.dashboardData.performanceIndicators?.quickRatio || 0,
        revenueGrowth: this.dashboardData.performanceIndicators?.revenueGrowth || 0,
      };

      // Update cash flow metrics
      this.cashFlowMetrics = {
        totalMoneyIn: this.dashboardData.cashFlowMetrics?.totalMoneyIn || 0,
        totalMoneyOut: this.dashboardData.cashFlowMetrics?.totalMoneyOut || 0,
        currentCashPosition: this.dashboardData.cashFlowMetrics?.currentCashPosition || 0,
        bankBalances: this.dashboardData.cashFlowMetrics?.bankBalances || {},
      };

      // Update all charts
      this.updateBankBalanceChart();
      this.updateCashFlowTrend();

      // Update invoice analysis
      this.invoiceAnalysis = {
        averageDiscount: this.dashboardData.invoiceAnalysis?.averageDiscount || 0,
        averageDiscountPercentage: this.dashboardData.invoiceAnalysis?.averageDiscountPercentage || 0,
        totalAdditions: this.dashboardData.invoiceAnalysis?.totalAdditions || 0,
        totalExpenses: this.dashboardData.invoiceAnalysis?.totalExpenses || 0,
        deferredInvoicesCount: this.dashboardData.invoiceAnalysis?.deferredInvoicesCount || 0,
        expensesByType: this.dashboardData.invoiceAnalysis?.expensesByType || {},
      };

      // Update item analysis
      if (this.dashboardData.invoiceItemAnalysis) {
        const analysis = this.dashboardData.invoiceItemAnalysis;
        this.itemAnalysis = {
          topSellingItemRevenue: analysis.topSellingItemRevenue || 0,
          topSellingItemName: analysis.topSellingItemName || '',
          itemCategoryDistribution: analysis.itemCategoryDistribution || {},
          itemProfitMargins: analysis.itemProfitMargins || [],
          topSellingItems: analysis.topSellingItems || [],
        };
      }

      // Update store and account analysis
      this.storeAccountAnalysis = {
        storeMetrics: this.dashboardData.storeAccountAnalysis?.storeMetrics || [],
        accountMetrics: this.dashboardData.storeAccountAnalysis?.accountMetrics || [],
      };
    }
  }

  private updateDailySalesTrend(): void {
    const salesData: any = this.dashboardData?.salesCharts?.find((chart: { chartType: string }) => chart.chartType === 'line');
    if (salesData) {
      this.dailySalesTrendOption = {
        ...this.getDefaultChartOptions('financialReports.charts.dailySales'),
        tooltip: {
          trigger: 'axis',
          formatter: (params: any) => {
            const data = Array.isArray(params) ? params[0] : params;
            return `${data.name}<br/>${data.seriesName}: $${data.value}`;
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
          name: 'Revenue ($)',
          axisLabel: {
            formatter: '${value}',
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
        ...this.getDefaultChartOptions('financialReports.charts.salesVsCosts'),
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow',
          },
          formatter: (params: any) => {
            return params.map((param: any) => `${param.seriesName}: $${param.value}`).join('<br/>');
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
          name: 'Amount ($)',
          axisLabel: {
            formatter: '${value}',
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
        ...this.getDefaultChartOptions('financialReports.charts.profitMargin'),
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

  private updateTopItemsByRevenue(): void {
    const revenueData: any = this.dashboardData?.invoiceItemAnalysis?.itemSalesCharts?.find(
      (chart: any) => chart.title === 'Top 10 Items by Revenue'
    );
    if (revenueData) {
      this.topItemsByRevenueOption = {
        ...this.getDefaultChartOptions('financialReports.charts.topItemsByRevenue'),
        tooltip: {
          trigger: 'axis',
          formatter: revenueData.options?.tooltip?.formatter || '{b}<br/>Revenue: ${c}',
        },
        xAxis: {
          type: 'value',
          name: 'Revenue ($)',
          axisLabel: {
            formatter: '${value}',
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
              formatter: '${c}',
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
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross',
            label: {
              backgroundColor: '#6a7985',
            },
          },
          formatter: (params: any) => {
            const data = Array.isArray(params) ? params[0] : params;
            const value = data.value || 0;
            return `${data.name}<br/>${data.seriesName}: ${value.toLocaleString('tr-TR', { style: 'currency', currency: 'USD' })}`;
          },
        },
        legend: {
          data: ['Net Cash Flow'],
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true,
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: cashFlowData.labels || [],
        },
        yAxis: {
          type: 'value',
          axisLabel: {
            formatter: (value: number) => value.toLocaleString('tr-TR', { style: 'currency', currency: 'TRY' }),
          },
        },
        series: [
          {
            name: 'Net Cash Flow',
            type: 'line',
            data: cashFlowData.series[0].data || [],
            areaStyle: {
              opacity: 0.3,
            },
            lineStyle: {
              width: 2,
            },
            itemStyle: {
              color: '#2f4554',
            },
            smooth: true,
          },
        ],
      };
    }
  }

  private updateBankBalance(): void {
    const balanceData: any = this.dashboardData?.bankBalanceCharts?.find((chart: any) => chart.chartType === 'bar');
    if (balanceData) {
      this.bankBalanceOption = {
        ...this.getDefaultChartOptions('financialReports.charts.bankBalance.title'),
        tooltip: {
          trigger: 'axis',
        },
        xAxis: {
          type: 'category',
          data: balanceData.xAxisData || [],
        },
        yAxis: {
          type: 'value',
          name: 'Balance ($)',
          axisLabel: {
            formatter: '${value}',
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
              formatter: '${c}',
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
        ...this.getDefaultChartOptions('financialReports.charts.topItemsByQuantity'),
        tooltip: {
          trigger: 'axis',
          formatter: quantityData.options?.tooltip?.formatter || '{b}<br/>Quantity: {c}',
        },
        xAxis: {
          type: 'value',
          name: this.translateService.instant('financialReports.topItems.quantity'),
        },
        yAxis: {
          type: 'category',
          data: quantityData.labels || [],
          name: quantityData.options?.yAxis?.[0]?.name || 'Items',
        },
        series: [
          {
            name: quantityData.series?.[0]?.name || this.translateService.instant('financialReports.topItems.quantity'),
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
        ...this.getDefaultChartOptions('financialReports.charts.performanceRatios.title'),
        tooltip: {
          trigger: 'item',
          formatter: (params: any) => {
            const value = params.value;
            const indicators = ['currentRatio', 'quickRatio', 'operatingMargin', 'netProfitMargin'];
            let result = `${params.name}<br/>`;
            indicators.forEach((indicator, index) => {
              const formattedValue = index >= 2 ? `${value[index].toFixed(2)}%` : value[index].toFixed(2);
              result += `${this.translateService.instant(
                `financialReports.charts.performanceRatios.metrics.${indicator}`
              )}: ${formattedValue}<br/>`;
            });
            return result;
          },
        },
        radar: {
          shape: 'circle',
          splitNumber: 4,
          axisName: {
            show: true,
            formatter: function (name?: string) {
              if (!name) return '';
              return name.length > 15 ? name.substring(0, 15) + '...' : name;
            },
          },
          splitArea: {
            show: true,
            areaStyle: {
              color: ['rgba(255,255,255,0.3)', 'rgba(200,200,200,0.3)'],
            },
          },
          axisLine: {
            show: true,
          },
          splitLine: {
            show: true,
          },
          indicator: [
            {
              name: this.translateService.instant('financialReports.charts.performanceRatios.metrics.currentRatio'),
              max: 4,
              min: 0,
            },
            {
              name: this.translateService.instant('financialReports.charts.performanceRatios.metrics.quickRatio'),
              max: 4,
              min: 0,
            },
            {
              name: `${this.translateService.instant('financialReports.charts.performanceRatios.metrics.operatingMargin')} (%)`,
              max: 40,
              min: 0,
            },
            {
              name: `${this.translateService.instant('financialReports.charts.performanceRatios.metrics.netProfitMargin')} (%)`,
              max: 40,
              min: 0,
            },
          ],
        },
        series: [
          {
            type: 'radar',
            data: [
              {
                value: [
                  this.performanceIndicators?.currentRatio || 0,
                  this.performanceIndicators?.quickRatio || 0,
                  this.performanceIndicators?.operatingMargin || 0,
                  this.performanceIndicators?.netProfitMargin || 0,
                ],
                name: this.translateService.instant('financialReports.charts.performanceRatios.title'),
                areaStyle: {
                  opacity: 0.3,
                },
                lineStyle: {
                  width: 2,
                },
                symbol: 'circle',
                symbolSize: 6,
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
        ...this.getDefaultChartOptions('financialReports.charts.marginTrends.title'),
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
        ...this.getDefaultChartOptions('financialReports.charts.profitBreakdown.title'),
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
              {
                value: this.performanceIndicators.grossProfitMargin,
                name: this.translateService.instant('financialReports.metrics.performanceIndicators.grossProfitMargin'),
              },
              {
                value: this.performanceIndicators.operatingExpensesRatio,
                name: this.translateService.instant('financialReports.metrics.performanceIndicators.operatingExpenses'),
              },
              {
                value: this.performanceIndicators.netProfitMargin,
                name: this.translateService.instant('financialReports.metrics.performanceIndicators.netProfitMargin'),
              },
            ],
          },
        ],
      };
    }
  }

  private updateBankBalanceChart(): void {
    const bankBalanceData = this.dashboardData?.cashFlowCharts?.find((chart: any) => chart.title === 'Bank Balance Distribution');

    if (bankBalanceData) {
      this.bankBalanceOption = {
        tooltip: {
          trigger: 'item',
          formatter: (params: any) => {
            const value = params.value || 0;
            const percent = params.percent || 0;
            return `${params.name}<br/>Balance: ${value.toLocaleString('tr-TR', {
              style: 'currency',
              currency: 'USD',
            })}<br/>Percentage: ${percent}%`;
          },
        },
        legend: {
          orient: 'vertical',
          right: 10,
          top: 'center',
        },
        series: [
          {
            name: 'Bank Balances',
            type: 'pie',
            radius: ['40%', '70%'],
            avoidLabelOverlap: true,
            itemStyle: {
              borderRadius: 10,
              borderColor: '#fff',
              borderWidth: 2,
            },
            label: {
              show: true,
              formatter: '{b}: {d}%',
            },
            emphasis: {
              label: {
                show: true,
                fontSize: '16',
                fontWeight: 'bold',
              },
            },
            labelLine: {
              show: true,
            },
            data: bankBalanceData.series[0].data.map((value: number, index: number) => ({
              value,
              name: bankBalanceData.labels[index],
            })),
          },
        ],
      };
    }
  }

  onPeriodChange(): void {
    this.loadData();
  }

  refreshData(): void {
    this.loading = true;
    this.loadData();
  }

  onAccountSelected(account: any): void {
    if (!account) {
      this.selectedAccount = undefined;
      return;
    }
    this.selectedAccount = account;
    this.loadData();
  }

  onBankSelected(bank: any): void {
    if (!bank) {
      this.selectedBank = undefined;
      return;
    }
    this.selectedBank = bank;
    this.loadData();
  }

  onStoreSelected(store: any): void {
    if (!store) {
      this.selectedStore = undefined;
      return;
    }
    this.selectedStore = store;
    this.loadData();
  }

  resetSelections(): void {
    this.selectedBank = undefined;
    this.selectedAccount = undefined;
    this.selectedStore = undefined;
    this.loadData();
  }

  private loadData(): void {
    this.loading = true;
    const searchDTO: FinancialSearchDTO = {
      startDate: this.dateRange.startDate.toISOString(),
      endDate: this.dateRange.endDate.toISOString(),
    };

    // Only add bankId if a bank is selected
    if (this.selectedBank) {
      searchDTO.bankId = this.selectedBank.id;
    }

    // Only add accountId if an account is selected
    if (this.selectedAccount) {
      searchDTO.accountId = this.selectedAccount.id;
    }

    // Only add storeId if a store is selected
    if (this.selectedStore) {
      searchDTO.storeId = this.selectedStore.id;
    }

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
        weekStart.setDate(today.getDate() - today.getDay() - 7); // Subtract an extra 7 days for one week before today
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

    this.onPeriodChange();
  }
}
