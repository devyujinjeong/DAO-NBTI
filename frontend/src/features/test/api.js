import api from "@/api/axios.js";

/* 1. 회원의 포인트를 가져오기 */
export function getUserPoints() {
    return api.get('/user/points');
}

/* 2. 문제 가져오기 */
export function getProblems() {
    return api.get('/test/problems');
}

/* 3. 문제 채점하기 */
export function submitAnswers(payload) {
    return api.post(`/test-result`, payload );
}

/* 4. 문제 결과 가져오기 */
export function getTestResult(testResultId) {
    return api.get(`/test-result/now/${testResultId}`, testResultId)
}

/* 5. 검사 결과 마이페이지에 저장 하기 */
export function saveResultToMyPage(testResultId) {
    return api.put(`/test-result/${testResultId}/my-page`);
}