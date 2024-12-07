import { Component, OnInit, AfterViewInit, ElementRef, ViewChildren, QueryList, OnDestroy } from '@angular/core';
import { FinancialReportsService } from 'app/core/konsolApi/api/financialReports.service';
import { FinancialDashboardDTO } from 'app/core/konsolApi/model/financialDashboardDTO';
import { FinancialSearchDTO } from 'app/core/konsolApi/model/financialSearchDTO';
import * as echarts from 'echarts';

@Component({
  selector: 'jhi-financial-reports',
  templateUrl: './financial-reports.component.html',
  styleUrls: ['./financial-reports.component.scss'],
})
export class FinancialReportsComponent implements OnInit, AfterViewInit, OnDestroy {
  @ViewChildren('chart') chartElements!: QueryList<ElementRef>;

  dashboardData?: any;
  loading = true;
  selectedDateRange = 'today';
  dateRange = {
    startDate: new Date(),
    endDate: new Date(),
  };

  metrics = {
    sales: {
      totalSales: 0,
      netSales: 0,
      netProfit: 0,
      dailyRevenue: 0,
      monthlyRevenue: 0,
    },
    cashFlow: {
      totalMoneyIn: 0,
      totalMoneyOut: 0,
      currentCashPosition: 0,
      bankBalances: {},
    },
    performance: {
      grossProfitMargin: 0,
      netProfitMargin: 0,
      operatingExpensesRatio: 0,
      currentRatio: 0,
      quickRatio: 0,
      revenueGrowth: 0,
    },
  };

  private charts: echarts.ECharts[] = [];
  private chartOptions: echarts.EChartsOption[] = [];

  constructor(private financialReportsService: FinancialReportsService) {}

  ngOnInit(): void {
    this.setDateRange('today');
    this.loadDashboardData();
  }

  ngAfterViewInit(): void {
    this.initializeCharts();
  }

  setDateRange(range: string): void {
    this.selectedDateRange = range;
    const today = new Date();
    today.setHours(0, 0, 0, 0);

    switch (range) {
      case 'today':
        this.dateRange = {
          startDate: today,
          endDate: new Date(),
        };
        break;
      case 'week':
        const weekStart = new Date(today);
        weekStart.setDate(today.getDate() - today.getDay());
        this.dateRange = {
          startDate: weekStart,
          endDate: new Date(),
        };
        break;
      case 'month':
        const monthStart = new Date(today.getFullYear(), today.getMonth(), 1);
        this.dateRange = {
          startDate: monthStart,
          endDate: new Date(),
        };
        break;
      case 'year':
        const yearStart = new Date(today.getFullYear(), 0, 1);
        this.dateRange = {
          startDate: yearStart,
          endDate: new Date(),
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
          this.updateMetrics(data);
          this.prepareChartOptions();
          this.updateCharts();
        }
        this.loading = false;
      },
      error: (error: any) => {
        console.error('Error loading dashboard data:', error);
        this.loading = false;
      },
    });
  }

  private updateMetrics(data: any): void {
    if (data.salesMetrics) {
      this.metrics.sales = {
        totalSales: data.salesMetrics.totalSales,
        netSales: data.salesMetrics.netSales,
        netProfit: data.salesMetrics.netProfit,
        dailyRevenue: data.salesMetrics.dailyRevenue,
        monthlyRevenue: data.salesMetrics.monthlyRevenue,
      };
    }

    if (data.cashFlowMetrics) {
      this.metrics.cashFlow = {
        totalMoneyIn: data.cashFlowMetrics.totalMoneyIn,
        totalMoneyOut: data.cashFlowMetrics.totalMoneyOut,
        currentCashPosition: data.cashFlowMetrics.currentCashPosition,
        bankBalances: data.cashFlowMetrics.bankBalances || {},
      };
    }

    if (data.performanceIndicators) {
      this.metrics.performance = {
        grossProfitMargin: data.performanceIndicators.grossProfitMargin,
        netProfitMargin: data.performanceIndicators.netProfitMargin,
        operatingExpensesRatio: data.performanceIndicators.operatingExpensesRatio,
        currentRatio: data.performanceIndicators.currentRatio,
        quickRatio: data.performanceIndicators.quickRatio,
        revenueGrowth: data.performanceIndicators.revenueGrowth,
      };
    }
  }

  private prepareChartOptions(): void {
    if (!this.dashboardData) return;

    this.chartOptions = [];

    if (this.dashboardData.salesCharts) {
      this.dashboardData.salesCharts.forEach((chart: any) => {
        this.chartOptions.push(this.createChartOption(chart));
      });
    }

    if (this.dashboardData.cashFlowCharts) {
      this.dashboardData.cashFlowCharts.forEach((chart: any) => {
        this.chartOptions.push(this.createChartOption(chart));
      });
    }

    if (this.dashboardData.performanceCharts) {
      this.dashboardData.performanceCharts.forEach((chart: any) => {
        this.chartOptions.push(this.createChartOption(chart));
      });
    }
  }

  private createChartOption(chartData: any): echarts.EChartsOption {
    const option: echarts.EChartsOption = {
      title: {
        text: chartData.title,
        subtext: chartData.subtitle,
        left: 'center',
      },
      tooltip: {
        trigger: 'axis',
        ...chartData.options?.tooltip,
      },
      legend: {
        data: chartData.series.map((s: any) => s.name),
        bottom: 0,
        show: chartData.options?.showLegend !== false,
      },
      xAxis: {
        type: 'category',
        data: chartData.labels,
        name: chartData.xAxisLabel,
      },
      yAxis: chartData.options?.yAxis || {
        type: 'value',
        name: chartData.yAxisLabel,
      },
      series: chartData.series.map((series: any) => ({
        name: series.name,
        type: series.type,
        data: series.data,
        ...series.style,
      })),
    };

    if (chartData.chartType === 'pie') {
      delete option.xAxis;
      delete option.yAxis;
      option.series = [
        {
          type: 'pie',
          radius: '50%',
          data: chartData.series[0].data.map((value: number, index: number) => ({
            value,
            name: chartData.labels[index],
          })),
        },
      ];
    }

    return option;
  }

  private initializeCharts(): void {
    if (!this.chartElements) return;

    this.charts.forEach(chart => chart.dispose());
    this.charts = [];

    this.chartElements.forEach((element, index) => {
      const chart = echarts.init(element.nativeElement);
      this.charts.push(chart);

      if (this.chartOptions[index]) {
        chart.setOption(this.chartOptions[index]);
      }

      const resizeObserver = new ResizeObserver(() => {
        chart.resize();
      });
      resizeObserver.observe(element.nativeElement);
    });
  }

  private updateCharts(): void {
    if (!this.charts.length || !this.chartOptions.length) {
      this.initializeCharts();
      return;
    }

    this.charts.forEach((chart, index) => {
      if (this.chartOptions[index]) {
        chart.setOption(this.chartOptions[index]);
      }
    });
  }

  ngOnDestroy(): void {
    this.charts.forEach(chart => chart.dispose());
  }

  formatCurrency(value: number): string {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD',
    }).format(value);
  }

  formatPercentage(value: number): string {
    return `${value.toFixed(2)}%`;
  }
}
