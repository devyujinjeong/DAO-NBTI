export const studyRoutes = [
    {
        path: '/study/result',
        name: 'StudyResult',
        component : () => import('@/features/study/views/StudyResultView.vue')
    },
    {
        path: '/study',
        name: 'Study',
        component : () => import('@/features/study/views/StudyView.vue')
    }
]