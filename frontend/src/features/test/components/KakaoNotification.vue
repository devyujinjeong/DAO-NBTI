<script setup>
import { onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const currentUrl = `${window.location.origin}${route.fullPath}`
const kakaoReady = ref(false)

async function loadKakaoSdk() {
    if (window.Kakao) return

    await new Promise((resolve, reject) => {
        const script = document.createElement('script')
        script.src = 'https://t1.kakaocdn.net/kakao_js_sdk/2.7.5/kakao.min.js'
        script.onload = resolve
        script.onerror = reject
        document.head.appendChild(script)
    })
}

onMounted(async () => {
    await loadKakaoSdk()
    if (window.Kakao && !window.Kakao.isInitialized()) {
        window.Kakao.init('391d106ca743d055628dc800f643d882')
        kakaoReady.value = true
    }
})

function shareToKakao() {
    if (!window.Kakao?.isInitialized()) {
        alert('Kakao SDK가 초기화되지 않았습니다.')
        return
    }

    window.Kakao.Share.sendDefault({
        objectType: 'text',
        text: `두뇌 검사 결과를 공유합니다!\n\n👇 결과 보기\n${currentUrl}`,
        link: {
            mobileWebUrl: currentUrl,
            webUrl: currentUrl
        },
        buttons: [
            {
                title: '결과 보러가기',
                link: {
                    mobileWebUrl: currentUrl,
                    webUrl: currentUrl
                }
            }
        ]
    })
}
</script>

<template>
    <a @click.prevent="shareToKakao">

    </a>
</template>
