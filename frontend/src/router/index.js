import { createRouter, createWebHistory } from 'vue-router'
import mainRoutes from '@/features/main/router.js'
import LayoutDefault from "@/components/layout/LayoutDefault.vue";
import mypageRoutes from '@/features/mypage/router.js'
import { adminRoutes } from "@/features/admin/router.js";
import { userRoutes } from "@/features/user/router.js";
import { studyRoutes } from "@/features/study/router.js";
import {testRoutes} from "@/features/test/router.js";
import { useAuthStore } from '@/stores/auth' // Pinia auth store 경로에 맞게 조정
import { useToast } from 'vue-toastification'

const router = createRouter({
    history: createWebHistory(),
    routes: [
        //...
        //나중에 다른 기능과 관련된 routes들을 추가하면 된다.
        {
            path: '/',
            component: LayoutDefault,
            children: [
                ...mainRoutes,
                ...mypageRoutes,
                ...adminRoutes,
                ...userRoutes,
                ...studyRoutes,
                ...testRoutes
            ]
        },

    ]
})

router.beforeEach((to, from, next) => {
    const auth = useAuthStore()
    const toast = useToast()

    const requiresAuth = to.matched.some(record => record.meta.requiresAuth)
    // 모든 매칭된 라우트의 roles 수집
    const requiredRoles = to.matched
        .flatMap(record => record.meta?.roles || [])


    if (requiresAuth && !auth.accessToken) {
        // 사용자에게 알림 (옵션)
        toast.error('로그인이 필요한 페이지입니다.')

        // 로그인 페이지로 이동 + 원래 목적지 기억
        next({ path: '/login', query: { redirect: to.fullPath } })
    }
    // 2. role 권한이 필요한데 현재 유저 role이 포함되어 있지 않으면
    if (requiredRoles.length > 0 && !requiredRoles.includes(auth.userRole)) {
        toast.error('접근 권한이 없습니다.')
        if (auth.userRole === 'ADMIN') {
            return next({ path: '/admin'})
        }
        return next({ path: '/' }) // 메인 페이지로 보내기
    }

        next()
})

export default router
