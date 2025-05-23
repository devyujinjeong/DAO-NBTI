import LoginView from "@/features/user/views/LoginView.vue";
import SignupView from "@/features/user/views/SignupView.vue";
import FindPaswordView from "@/features/user/views/FindPaswordView.vue";
import ResetPasswordView from "@/features/user/views/ResetPasswordView.vue";


export const userRoutes = [
    {
        path: '/login',
        name: 'LoginView',
        component: LoginView
    },
    {
        path: '/signup',
        name: 'SignupView',
        component: SignupView
    },
    {
        path: '/find-password',
        name: 'FindPassword',
        component: FindPaswordView
    },
    {
        path: '/reset-password',
        name: 'ResetPassword',
        component: ResetPasswordView
    }
]