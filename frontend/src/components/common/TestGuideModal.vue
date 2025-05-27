<script setup>
import { nextTick, watch } from 'vue'

const props = defineProps({
    visible: { type: Boolean, required: true },
    cancelText: { type: String, default: '닫기' },
    confirmText: { type: String, default: '확인' }
})

const emit = defineEmits(['confirm', 'cancel'])

function handleConfirm() {
    emit('confirm')
}

function handleCancel() {
    emit('cancel')
}

function focusModal() {
    nextTick(() => {
        const modal = document.querySelector('.modal')
        if (modal) modal.focus()
    })
}

watch(() => props.visible, (val) => {
    if (val) focusModal()
})
</script>

<template>
    <div
        v-if="visible"
        class="modal"
        tabindex="0"
        @keydown.enter="handleConfirm"
        @keydown.esc="handleCancel"
    >
        <div class="modal-content">
            <div class="guide-content">
                <h2>문제 풀이 안내</h2>
                <p>
                    문제 풀이 안내 사항입니다. <br/>
                    반드시 안내를 확인하고 문제풀이를 진행하세요.
                </p>
                <div class="correct-example">
                    ✅ <strong>올바른 예시</strong><br/>
                    객관식 : <strong>1</strong> (숫자만 입력)<br/>
                    단답형 : ACEBD (연속 입력)<br/>
                </div>
                <div class="wrong-example">
                    ❌ <strong>오답 예시</strong><br/>
                    객관식 : 1번, 2번 같은 표기 또는 숫자 + 문자 조합도 X<br/>
                    단답형 : A, B, C / A-B-C / A B C (쉼표, 하이픈, 띄어쓰기 X)
                </div>
                <p class="important">
                    📢 <strong>꼭 확인하세요!</strong><br/>
                    안내를 제대로 읽지 않아 발생하는 문제는 책임지지 않습니다. <br/>
                    안내 내용을 충분히 이해한 뒤 검사를 진행해주세요.
                </p>
            </div>

            <div class="modal-buttons">
                <button class="confirm-btn" @click="handleConfirm">{{ confirmText }}</button>
                <button class="cancel-btn" @click="handleCancel">{{ cancelText }}</button>
            </div>
        </div>
    </div>
</template>

<style scoped>
.modal {
    position: fixed;
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 1000;
    left: 0;
    top: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.4);
    outline: none;
}

.modal-content {
    background-color: #fff;
    margin: 10% auto;
    padding: 20px;
    border-radius: 12px;
    width: 500px;
    text-align: center;
}

.modal-content h2 {
    margin-bottom: 1rem;
    font-size: 1.6rem;
}

.modal-content p {
    font-size: 16px;
    margin-bottom: 1rem;
    text-align: center;
    white-space: pre-line;
    word-break: keep-all;
    line-height: 1.6;
}

.correct-example, .wrong-example {
    background-color: #f0f4ff;
    padding: 1rem;
    border-radius: 8px;
    margin: 1rem 0;
    font-weight: bold;
}

.important {
    padding: 1rem;
    border-radius: 8px;
    font-weight: bold;
}

.modal-buttons {
    display: flex;
    justify-content: space-evenly;
    margin-top: 1rem;
}

.modal-buttons button {
    padding: 6px 16px;
    font-size: 16px;
    border-radius: 4px;
    cursor: pointer;
    border: none;
    background-color: #3B82F6;
    color: #fff;
    transition: background 0.2s ease;
}

.modal-buttons button:hover {
    background-color: #1e3a8a;
}
</style>
