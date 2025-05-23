import { createRouter, createWebHistory } from 'vue-router'
import mainRoutes from '@/features/main/router.js'
import LayoutDefault from "@/components/layout/LayoutDefault.vue";
import mypageRoutes from '@/features/mypage/router.js'
import { adminRoutes } from "@/features/admin/router.js";
import { userRoutes } from "@/features/user/router.js";
import { studyRoutes } from "@/features/study/router.js";
import {testRoutes} from "@/features/test/router.js";

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

export default router