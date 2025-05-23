<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { useToast } from 'vue-toastification'
import linkIcon from '@/assets/images/link_icon.png'
import kakaoIcon from '@/assets/images/kakao_button.png'

const props = defineProps({
    visible: Boolean,
    message: String
})

const emit = defineEmits(['close']);
const toast = useToast();
const route = useRoute();
const currentUrl = `${window.location.origin}${route.fullPath}`;
const kakaoReady = ref(false);

// SDK 로딩
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
        // key 값 넣기
        window.Kakao.init('391d106ca743d055628dc800f643d882')
        kakaoReady.value = true
    }
})

// 공유 링크 복사
function copyLink() {
    navigator.clipboard.writeText(props.message)
    toast.success('공유 링크가 복사되었습니다.')
    emit('close')
}

// 카카오톡 공유
function handleKakaoShare() {
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

    emit('close')
}
</script>


<template>
    <div v-if="visible" class="share-modal">
        <div class="share-header">
            <span>공유하기</span>
            <button class="close-btn" @click="$emit('close')">✕</button>
        </div>
        <hr />
        <div class="share-options">
            <div class="option" @click="handleKakaoShare" >
                <img :src="kakaoIcon" alt="카카오 공유"/>
                <p>카카오톡</p>
            </div>
            <div class="option" @click="copyLink">
                <img :src="linkIcon" alt="링크복사" />
                <p>링크복사</p>
            </div>
        </div>
    </div>
</template>

<style scoped>
.share-modal {
    position: fixed;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    width: 260px;
    background: #fff;
    border-radius: 16px;
    padding: 1rem 1.2rem;
    box-shadow: 0 6px 20px rgba(0, 0, 0, 0.15);
    z-index: 9999;
    font-family: 'Pretendard', sans-serif;
}

.share-header {
    position: relative;
    height: 32px;
    margin-bottom: 0.5rem;
}

.share-header span {
    position: absolute;
    left: 50%;
    transform: translateX(-50%);
    font-weight: 600;
    font-size: 16px;
}

.close-btn {
    position: absolute;
    right: 0;
    top: 0;
    background: transparent;
    border: none;
    font-size: 16px;
    cursor: pointer;
}

hr {
    border: none;
    border-top: 1px solid #eee;
    margin-bottom: 1rem;
}

.share-options {
    display: flex;
    justify-content: space-between;
    gap: 0.75rem;
}

.option {
    flex: 1;
    display: flex;
    flex-direction: column;
    align-items: center;
    font-size: 14px;
    color: #333;
    cursor: pointer;
    padding: 0.5rem;
    border-radius: 12px;
    transition: background 0.2s;
}

.option:hover {
    background: #f3f4f6;
}

.option img {
    width: 48px;
    height: 48px;
    object-fit: contain;
    border-radius: 50%;
    margin-bottom: 0.5rem;
    background: #f5f5f5;
    padding: 6px;
}

.option p {
    margin: 0;
    padding: 0;
    font-size: 13px;
    color: #333;
}
</style>
