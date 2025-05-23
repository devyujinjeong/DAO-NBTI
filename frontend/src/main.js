
import { createApp } from 'vue'
import App from './App.vue'
import { createPinia } from 'pinia'
import router from "@/router/index.js";
import {useAuthStore} from "@/stores/auth.js";
import Toast from 'vue-toastification'
import 'vue-toastification/dist/index.css'
import {refreshUserToken} from "@/features/user/api.js";

async function bootstrap() {
    const app = createApp(App)

    app.use(createPinia())

    // ✅ Pinia 생성 후 auth 상태 복구
    const authStore = useAuthStore()
    try {
        const resp = await refreshUserToken()
        authStore.setAuth(resp.data.data.accessToken)
    } catch (e) {
        console.log("리프레시 실패");
    }

    app
        .use(router)
        .use(Toast, {
            position: 'top-right',
            timeout: 3000,
        })
        .mount('#app')
}

bootstrap();