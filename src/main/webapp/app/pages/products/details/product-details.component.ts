import { Component, OnInit, ViewChild, ElementRef, AfterViewInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { finalize } from 'rxjs/operators';
import { ItemResourceService } from '../../../core/konsolApi/api/itemResource.service';
import { ItemDTO, ItemAnalysisDTO, ChartDataContainer, ChartSearchDTO, ItemAnalysisSearchDTO } from '../../../core/konsolApi';
import {
  Chart,
  ChartConfiguration,
  LineController,
  LineElement,
  PointElement,
  LinearScale,
  CategoryScale,
  Legend,
  Tooltip,
} from 'chart.js';
import { TranslateService } from '@ngx-translate/core';
const dateFns = require('date-fns');

// Register required components
Chart.register(LineController, LineElement, PointElement, LinearScale, CategoryScale, Legend, Tooltip);

// Add interface for chart data
interface ChartItem {
  date: string;
  totalSales: number;
  totalQty: number;
  avgPrice: number;
}

@Component({
  selector: 'app-product-details',
  templateUrl: './product-details.component.html',
})
export class ProductDetailsComponent implements OnInit, AfterViewInit {
  @ViewChild('chartCanvas') chartCanvas!: ElementRef;

  product?: ItemDTO;
  productAnalysis?: ItemAnalysisDTO;
  isLoading = false;
  loadingChart = false;
  chartData?: ChartDataContainer;
  chart?: Chart;

  dateRanges = [
    { label: 'Week', value: 'week' },
    { label: 'Month', value: 'month' },
    { label: 'Year', value: 'year' },
  ];
  selectedRange = 'week';

  constructor(private route: ActivatedRoute, private itemService: ItemResourceService, private translateService: TranslateService) {}

  ngOnInit(): void {
    this.route.data.subscribe(data => {
      this.product = data['item'];
      if (this.product?.id) {
        this.loadProductAnalysis(this.product.id);
      }
    });
  }

  ngAfterViewInit(): void {
    if (this.product?.id) {
      setTimeout(() => {
        // Add null check
        const id = this.product?.id;
        if (id) {
          this.loadChartData(id);
        }
      });
    }
  }

  onRangeChange(range: string): void {
    this.selectedRange = range;
    if (this.product?.id) {
      this.loadChartData(this.product.id);
    }
  }

  private getDateRange(): { startDate: string; endDate: string } {
    const endDate = new Date();
    let startDate: Date;

    switch (this.selectedRange) {
      case 'week':
        startDate = dateFns.subDays(endDate, 7);
        break;
      case 'month':
        startDate = dateFns.subMonths(endDate, 1);
        break;
      case 'year':
        startDate = dateFns.subYears(endDate, 1);
        break;
      default:
        startDate = dateFns.subDays(endDate, 7);
    }

    return {
      startDate: dateFns.startOfDay(startDate).toISOString(),
      endDate: endDate.toISOString(),
    };
  }

  private loadChartData(id: string): void {
    this.loadingChart = true;
    const { startDate, endDate } = this.getDateRange();

    const searchDTO: ChartSearchDTO = {
      itemId: id,
      startDate,
      endDate,
    };

    this.itemService
      .getItemCharts(id, searchDTO)
      .pipe(finalize(() => (this.loadingChart = false)))
      .subscribe({
        next: (data: ChartDataContainer) => {
          this.chartData = data;
          this.updateChart();
        },
        error: (error: any) => {
          console.error('Error loading chart data:', error);
        },
      });
  }

  private formatChartDate(dateStr: string): string {
    const date = new Date(dateStr);
    switch (this.selectedRange) {
      case 'week':
        return dateFns.format(date, 'MMM dd');
      case 'month':
        return dateFns.format(date, 'MMM dd');
      case 'year':
        return dateFns.format(date, 'MMM yyyy');
      default:
        return dateFns.format(date, 'MMM dd');
    }
  }

  private updateChart(): void {
    if (!this.chartData?.result || !this.chartCanvas) {
      return;
    }

    const ctx = this.chartCanvas.nativeElement.getContext('2d');

    if (this.chart) {
      this.chart.destroy();
    }

    const data = this.chartData.result;

    // Group data by date for month and year views
    let chartData: ChartItem[] = data.map(item => ({
      date: item.date || '',
      totalSales: item.totalSales || 0,
      totalQty: item.totalQty || 0,
      avgPrice: item.avgPrice || 0,
    }));

    if (this.selectedRange === 'month' || this.selectedRange === 'year') {
      const groupedData = new Map<
        string,
        {
          date: string;
          totalSales: number;
          totalQty: number;
          totalAmount: number;
          count: number;
        }
      >();

      chartData.forEach(item => {
        const date = new Date(item.date);
        const key = this.selectedRange === 'year' ? dateFns.format(date, 'yyyy-MM') : dateFns.format(date, 'yyyy-MM-dd');

        if (!groupedData.has(key)) {
          groupedData.set(key, {
            date: item.date,
            totalSales: 0,
            totalQty: 0,
            totalAmount: 0,
            count: 0,
          });
        }

        const group = groupedData.get(key)!;
        group.totalSales += item.totalSales;
        group.totalQty += item.totalQty;
        group.totalAmount += item.totalSales * item.avgPrice;
        group.count++;
      });

      chartData = Array.from(groupedData.values()).map(group => ({
        date: group.date,
        totalSales: group.totalSales,
        totalQty: group.totalQty,
        avgPrice: group.totalAmount / (group.totalSales || 1),
      }));
    }

    const config: ChartConfiguration<'line'> = {
      type: 'line',
      data: {
        labels: chartData.map(item => this.formatChartDate(item.date)),
        datasets: [
          {
            label: 'Sales Amount',
            data: chartData.map(item => item.totalSales),
            borderColor: 'rgb(75, 192, 192)',
            tension: 0.1,
            fill: false,
          },
          {
            label: 'Quantity Sold',
            data: chartData.map(item => item.totalQty),
            borderColor: 'rgb(255, 99, 132)',
            tension: 0.1,
            fill: false,
          },
          {
            label: 'Average Price',
            data: chartData.map(item => item.avgPrice),
            borderColor: 'rgb(54, 162, 235)',
            tension: 0.1,
            fill: false,
          },
        ],
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: {
            position: 'top',
          },
          tooltip: {
            mode: 'index',
            intersect: false,
            callbacks: {
              label: context => {
                let label = context.dataset.label || '';
                if (label) {
                  label += ': ';
                }
                if (context.parsed.y !== null) {
                  label +=
                    context.dataset.label?.includes('Price') || context.dataset.label?.includes('Amount')
                      ? new Intl.NumberFormat('en-US', { style: 'currency', currency: 'EGP' }).format(context.parsed.y)
                      : new Intl.NumberFormat('en-US').format(context.parsed.y);
                }
                return label;
              },
            },
          },
        },
        scales: {
          y: {
            beginAtZero: true,
            ticks: {
              callback: value => {
                return new Intl.NumberFormat('en-US', {
                  notation: 'compact',
                  compactDisplay: 'short',
                }).format(value as number);
              },
            },
          },
          x: {
            display: true,
            title: {
              display: true,
              text: 'Date',
            },
          },
        },
        interaction: {
          mode: 'nearest',
          axis: 'x',
          intersect: false,
        },
      },
    };

    this.chart = new Chart(ctx, config);
  }

  private loadProductAnalysis(id: string): void {
    this.isLoading = true;

    const searchDTO: ItemAnalysisSearchDTO = {
      itemId: id,
      // You can optionally add these parameters
      // storeId: undefined,
      // startDate: undefined,
      // endDate: undefined
    };

    this.itemService
      .getItemsAnalysis(id, searchDTO)
      .pipe(finalize(() => (this.isLoading = false)))
      .subscribe({
        next: (analysis: ItemAnalysisDTO) => {
          this.productAnalysis = analysis;
        },
        error: (error: any) => {
          console.error('Error loading product analysis:', error);
        },
      });
  }
}
