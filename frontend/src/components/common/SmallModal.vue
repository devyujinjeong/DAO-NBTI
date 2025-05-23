<!-- 작은 모달 -->
<script setup>
import { nextTick, watch } from 'vue'

const props = defineProps({
    visible: { type: Boolean, required: true },
    cancelText :{ type: String, default: '닫기' },
    confirmVisible: { type: Boolean, default: false },
    confirmText: { type: String, default: '확인' }
})

const emit = defineEmits(['confirm', 'cancel'])

function handleConfirm() {
    emit('confirm')
}

function handleCancel() {
    emit('cancel')
}

// 모달 열릴 때 자동 포커스
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
            <slot/>
            <div class="modal-buttons">
                <button v-if="confirmVisible" class="confirm-btn" @click="handleConfirm">{{ confirmText }}</button>
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
    margin: 15% auto;
    padding: 20px;
    border-radius: 12px;
    width: 300px;
    text-align: center;
}

.modal-content p {
    font-size: 16px;
    margin-bottom: 20px;
    text-align: center;
    white-space: pre-line;
    word-break: keep-all;
}

.modal-buttons {
    display: flex;
    justify-content: space-evenly;

}

.modal-buttons button {
    padding: 6px 16px;
    font-size: 12px;
    border-radius: 4px;
    cursor: pointer;
}

.cancel-btn, .confirm-btn {
    width: 70px;
    border: none;
    background-color: #3B82F6;
    color: #fff;
}
</style>
