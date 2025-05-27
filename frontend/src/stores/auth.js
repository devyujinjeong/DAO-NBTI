import {computed, ref} from "vue";
import {defineStore} from "pinia";

export const useAuthStore = defineStore('auth', () => {
    const accessToken = ref(null);
    const userRole = ref(null);
    const expirationTime = ref(null);
    const tempAccessToken = ref(null);

    // 인증 되어 있는지 확인하는 getter 값
    const isAuthenticated = computed(() =>
        !!accessToken.value && Date.now() < (expirationTime.value || 0)
    )

    function setAuth(at) {
        accessToken.value = at;
        try {
            const payload = JSON.parse(atob(at.split('.')[1]));
            console.log('payload', payload);
            userRole.value = payload.role;
            expirationTime.value = payload.exp * 1000;
        } catch (e) {
            accessToken.value = null;
            userRole.value = null;
            expirationTime.value = null;
        }
    }

    function setTempAuth(at) {
        tempAccessToken.value = at;
        accessToken.value = null;
        userRole.value = null;
        expirationTime.value = null;
    }

    function clearAuth() {
        accessToken.value = null;
        userRole.value = null;
        expirationTime.value = null;
        tempAccessToken.value = null;
        setTempAuth.value = null;
    }

    return {
        accessToken,
        userRole,
        expirationTime,
        isAuthenticated,
        tempAccessToken,
        setAuth,
        clearAuth,
        setTempAuth
    }

});