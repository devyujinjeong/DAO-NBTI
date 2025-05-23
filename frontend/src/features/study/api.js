import api from "@/api/axios.js";

export function getProblems(categoryId, level) {
    return api.get('/study/problem', {
        params: {
            categoryId,
            level
        }
    });
}

export function submitAnswers(payload) {
    return api.post('/study/submit', payload)
}

export function getStudyResult(studyId) {
    return api.get(`/study/result/${studyId}`);
}