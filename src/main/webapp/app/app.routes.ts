{
  path: 'public/license',
  loadChildren: () => import('./pages/license/license.module').then(m => m.LicenseModule),
  data: {
    authorities: [],  // No authority required
    pageTitle: 'license.title'
  }
} 