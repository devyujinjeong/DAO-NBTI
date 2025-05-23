import api from '@/api/axios.js';

// export const fetchProblemList = async (params) => {
//     try {
//         const response = await api.get('/admin/problems', { params });
//         return response.data.data.problems || [];
//     } catch (error) {
//         console.error('문제 목록 조회 실패:', error);
//         return [];
//     }
// };
//
// export const getAllMeetings = async () => {
//     const res = await api.get('/admin/problems');
//     return res.data.data.problems; // ApiResponse<>
// };

export function fetchUserList(queryParams) {
    return api.get(`/user/list?`+queryParams);
}

export function fetchTestResultList(queryParams) {
    return api.get(`/test-result/list/admin?`+queryParams);
}

export function fetchTestResultDetail(id){
    return api.get(`/test-result/${id}/admin`);
}
export function fetchStudyResult(queryParams){
    return api.get(`/admin/study?${queryParams}`);
}
export function fetchStudyResultDetail(studyResultId){
    return api.get(`/study/result/${studyResultId}`)
}
