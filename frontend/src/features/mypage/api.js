// src/features/mypage/api.js
import api from '@/api/axios.js'

/**
 * 검사 결과 목록 조회
 */
export function fetchTestResultList(params = {}) {
    return api.get('/test-result/list', { params })
}

/**
 * 검사 결과 상세 조회
 */
export function fetchTestDetail(testResultId) {
    return api.get(`/test-result/${testResultId}`)
}

/**
 * 학습 내역 조회
 */
export function fetchStudyResults(params = {}) {
    return api.get('/mypage/studies', { params })
}

/**
 * 학습 분야 목록 조회
 */
export function fetchStudyCategories() {
    return api.get('/study/category')
}

/**
 * 학습 세부 조회
 * @param {number|string} studyId
 */
export function fetchStudyDetail(studyId) {
    return api.get(`/mypage/studies/${studyId}`)
}

/**
 * 학습 문제 이의제기 신청
 * @param {{ problemId: number, reason: string }} payload
 */
export function submitObjection(payload) {
    return api.post('/mypage/objections', payload)
}

/**
 * 이의 제기 목록 조회
 * @param {{ status?: string, page?: number, size?: number }} params
 */
export function fetchObjections(params = {}) {
    return api.get('/mypage/objections', { params })
}

/**
 * 이의 제기 상세 조회
 * @param {number} objectionId
 */
export function fetchObjectionDetail(objectionId) {
    return api.get(`/mypage/objections/${objectionId}`)
}

/**
* 회원 정보 조회
 **/
export const fetchUserInfo = () => api.get('/user/info')

/**
 * 회원 탈퇴
 **/
export const fetchUserWithdraw = (password) => api.delete(
    '/user/withdraw', {
        data: {
            password: password
        }
    }
)