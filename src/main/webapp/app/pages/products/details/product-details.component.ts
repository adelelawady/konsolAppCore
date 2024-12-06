import { Component, OnInit, ViewChild, ElementRef, AfterViewInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { finalize } from 'rxjs/operators';
import { ItemResourceService } from '../../../core/konsolApi/api/itemResource.service';
import { ItemDTO, ItemAnalysisDTO, ChartDataContainer, ChartSearchDTO, ItemAnalysisSearchDTO } from '../../../core/konsolApi';
import { Chart, ChartConfiguration, registerables } from 'chart.js';
import { TranslateService } from '@ngx-translate/core';
const dateFns = require('date-fns');

// Register all Chart.js components
Chart.register(...registerables);

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
      // Wait for the next tick to ensure canvas is ready
      setTimeout(() => {
        const id = this.product?.id;
        if (id) {
          this.loadChartData(id);
        }
      }, 0);
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
          console.log('Raw Chart Data:', data);
          if (data?.result?.length) {
            this.chartData = data;
            setTimeout(() => {
              this.updateChart();
            }, 0);
          } else {
            console.warn('No chart data available');
          }
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
    if (!this.chartCanvas) {
      console.warn('Canvas element not found');
      return;
    }

    const ctx = this.chartCanvas.nativeElement.getContext('2d');
    if (!ctx) {
      console.error('Could not get canvas context');
      return;
    }

    // Destroy existing chart if it exists
    if (this.chart) {
      this.chart.destroy();
    }

    // Ensure we have data to display
    if (!this.chartData?.result?.length) {
      console.warn('No chart data available');
      return;
    }

    try {
      // Process and sort data
      const chartData = this.chartData.result
        .filter((item): item is typeof item & { date: string } => {
          return typeof item.date === 'string' && item.date !== null;
        })
        .map(item => ({
          date: item.date,
          totalSales: Number(item.totalSales) || 0,
          totalQty: Number(item.totalQty) || 0,
          avgPrice: Number(item.avgPrice) || 0,
        }))
        .sort((a, b) => new Date(a.date).getTime() - new Date(b.date).getTime());

      // Create new chart
      this.chart = new Chart(ctx, {
        type: 'line',
        data: {
          labels: chartData.map(item => this.formatChartDate(item.date)),
          datasets: [
            {
              label: this.translateService.instant('product.details.chart.labels.salesAmount'),
              data: chartData.map(item => item.totalSales),
              borderColor: 'rgb(75, 192, 192)',
              backgroundColor: 'rgba(75, 192, 192, 0.1)',
              tension: 0.1,
              fill: true,
              pointRadius: 4,
              pointHoverRadius: 6,
            },
            {
              label: this.translateService.instant('product.details.chart.labels.quantitySold'),
              data: chartData.map(item => item.totalQty),
              borderColor: 'rgb(255, 99, 132)',
              backgroundColor: 'rgba(255, 99, 132, 0.1)',
              tension: 0.1,
              fill: true,
              pointRadius: 4,
              pointHoverRadius: 6,
            },
          ],
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          interaction: {
            intersect: false,
            mode: 'index',
          },
          scales: {
            x: {
              grid: {
                display: false,
              },
            },
            y: {
              beginAtZero: true,
              grid: {
                color: 'rgba(0, 0, 0, 0.1)',
              },
            },
          },
          plugins: {
            legend: {
              position: 'top',
            },
            tooltip: {
              backgroundColor: 'rgba(255, 255, 255, 0.9)',
              titleColor: '#000',
              bodyColor: '#666',
              borderColor: 'rgba(0, 0, 0, 0.1)',
              borderWidth: 1,
              padding: 10,
              displayColors: true,
              callbacks: {
                label: context => {
                  const value = context.raw as number;
                  const label = context.dataset.label || '';
                  return `${label}: ${value.toLocaleString()}`;
                },
              },
            },
          },
        },
      });
    } catch (error) {
      console.error('Error creating chart:', error);
    }
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
