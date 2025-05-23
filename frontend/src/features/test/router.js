export const testRoutes = [
    {
        path: '/test',
        name: 'TestStart',
        component: () => import('@/features/test/views/TestView.vue')
    },
    {
        path: '/now/test-result/:testResultId',
        name: 'NowTestResult',
        component: () => import('@/features/test/views/TestResultView.vue')
    }
]