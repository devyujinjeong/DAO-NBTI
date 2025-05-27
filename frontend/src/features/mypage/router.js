import LayoutMypage from './components/LayoutMypage.vue'
import TestResultListView from './views/TestResultListView.vue'
import TestDetailView from './views/TestResultDetailView.vue'
import StudyListView from './views/StudyListView.vue'
import StudyDetailView from './views/StudyDetailView.vue'
import ObjectionListView from './views/ObjectionListView.vue'
import UserInfoView from "@/features/mypage/views/UserInfoView.vue";


export default [
    {
        path: '/mypage',
        component: LayoutMypage,
        meta: { requiresAuth: true },
        children: [
            { path: 'mypage', redirect: 'test'},
            { path: 'test', name: 'TestResultListView', component: TestResultListView },
            { path: 'test/:id', name: 'TestDetailView', component: TestDetailView },
            { path: 'study', name: 'StudyListView', component: StudyListView },
            { path: 'study/:id', name: 'StudyDetailView', component: StudyDetailView },
            { path: 'objection', name: 'ObjectionListView', component: ObjectionListView },
            { path: 'userinfo', name: 'UserInfoView', component: UserInfoView },
        ]
    }
]
