import { Component, OnInit, ViewChild, ElementRef, AfterViewInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { finalize } from 'rxjs/operators';
import { ItemResourceService } from '../../../core/konsolApi/api/itemResource.service';
import { ItemDTO, ItemAnalysisDTO, ChartDataContainer, ChartSearchDTO, ItemAnalysisSearchDTO } from '../../../core/konsolApi';
import { Chart, ChartConfiguration } from 'chart.js';
const dateFns = require('date-fns');

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

  constructor(private route: ActivatedRoute, private itemService: ItemResourceService) {}

  ngOnInit(): void {
    this.route.data.subscribe(data => {
      this.product = data['item'];
      if (this.product?.id) {
        this.loadProductAnalysis(this.product.id);
        this.loadChartData(this.product.id);
      }
    });
  }

  ngAfterViewInit(): void {
    if (this.product?.id) {
      this.loadChartData(this.product.id);
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
      startDate: dateFns.format(dateFns.startOfDay(startDate), 'yyyy-MM-dd'),
      endDate: dateFns.format(endDate, 'yyyy-MM-dd'),
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

  private updateChart(): void {
    if (!this.chartData?.result || !this.chartCanvas) {
      return;
    }

    const ctx = this.chartCanvas.nativeElement.getContext('2d');

    if (this.chart) {
      this.chart.destroy();
    }

    const data = this.chartData.result;
    const config: ChartConfiguration<'line'> = {
      type: 'line',
      data: {
        labels: data.map(item => item.date),
        datasets: [
          {
            label: 'Sales Amount',
            data: data.map(item => item.totalSales || 0),
            borderColor: 'rgb(75, 192, 192)',
            tension: 0.1,
            fill: false,
          },
          {
            label: 'Quantity Sold',
            data: data.map(item => item.totalQty || 0),
            borderColor: 'rgb(255, 99, 132)',
            tension: 0.1,
            fill: false,
          },
          {
            label: 'Average Price',
            data: data.map(item => item.avgPrice || 0),
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
          },
        },
        scales: {
          y: {
            beginAtZero: true,
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
