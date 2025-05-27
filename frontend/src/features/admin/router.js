export const adminRoutes = [
    {
        path: '/admin',
        component: () => import('@/features/admin/components/AdminLayout.vue'),
        meta: { requiresAuth: true, roles: ['ADMIN'] },
        children: [
            {path: '', redirect: '/admin/user'},
            {
                path: 'user',
                name: 'manage-user',
                component: () => import('@/features/admin/views/UserListView.vue')
            },
            {
                path: 'problems',
                name: 'manage-problem',
                component: () => import('@/features/admin/views/ProblemSearchView.vue'),
            },
            {
                path: 'problems/:problemId',
                name: 'problem-details',
                component: () => import('@/features/admin/views/ProblemDetailsView.vue')
            },
            {
                path: 'problems/new',
                name: 'create-problem',
                component: () => import('@/features/admin/views/ProblemCreateView.vue')
            },
            {
                path: 'objections',
                name: 'manage-objection',
                component: () => import('@/features/admin/views/ObjectionSearchView.vue')
            },
            {
                path: 'objections/:objectionId',
                name: 'objection-details',
                component: () => import('@/features/admin/views/ObjectionDetailsView.vue')
            }, {
                path: 'test',
                name: 'test-list',
                component: () => import('@/features/admin/views/TestListView.vue')
            },
            {
                path: 'test/:id',
                name: 'test-result',
                component: () => import('@/features/admin/views/AdminTestDetailView.vue')
            },
            {
                path: 'study',
                name: 'study-list',
                component: () => import('@/features/admin/views/StudyListView.vue')
            },
            {
                path: 'study/:id',
                name: 'study-result',
                component: () => import('@/features/admin/views/AdminStudyDetailView.vue')
            }

        ]
    }
];
