# 🕊️ 온기록 (On-girok) – 음성 기반 가족 자서전 플랫폼

> 가족 구성원의 **음성을 기록하고 세대 간 대화의 스토리를 보관**하는  
> 감정 기반 AI 자서전 플랫폼입니다.  
> 음성을 텍스트로 변환(STT)하고 GPT-4로 요약·감정 분석하여,  
> 가족의 이야기를 글로 남기는 따뜻한 서비스를 목표로 합니다.

---

## 📆 Project Info

- **기간:** 2025.03 ~ 2025.06  
- **팀명:** Fluxion  
- **인원:** 4명 (PM 1 · 프론트엔드 1 · 백엔드 2)  
- **담당 역할:** 백엔드 및 AI 연동 총괄 (기여도 약 45%)  
---

## 🎯 Overview

온기록은 음성을 중심으로 한 **가족 기억 저장 서비스**입니다.  
OpenAI Whisper로 음성을 텍스트로 변환하고, GPT-4로 요약 및 감정 분석을 수행합니다.  
가족 단위로 그룹을 구성하여 세대 간 이야기를 공유하고,  
‘음성에서 글로, 감정에서 기억으로’ 이어지는 따뜻한 아카이브를 제공합니다.

---

## 🧠 Main Features

### 🗂️ 가족 그룹 관리
- 가족 초대 및 구성원 관리  
- 그룹 생성 / 수정 / 삭제 / 멤버 조회 기능  
- 권한 기반 접근 제어 (Admin / Member)

### 📖 자서전 기록 (Chapter & Question)
- 질문(Question)을 기반으로 한 회상형 대화 진행  
- Whisper API로 음성 → 텍스트 변환  
- GPT-4 API로 텍스트 요약 및 감정 분석  
- 결과를 “자서전 단락(Chapter)” 형태로 자동 저장  

### 🎙️ 음성 처리 및 저장
- 음성 파일을 AWS S3에 업로드 및 URL 저장  
- Whisper API 호출 비동기 처리로 변환 속도 향상  
- 감정 분석 결과를 JSON 형태로 구조화하여 DB에 저장  

### 🧾 표준화된 응답 및 예외 처리
- `ApiResponse`, `GeneralException`, `ErrorStatus` 구조로 통합 관리  
- Controller-DTO-Service 레이어 분리로 유지보수성 강화  

---

## 🛠️ Tech Stack

| Category | Stack |
|-----------|--------|
| **Backend** | Spring Boot, JPA, PostgreSQL |
| **AI/ML** | OpenAI Whisper (STT), GPT-4 |
| **Infra** | AWS EC2 · RDS · S3 |
| **Tools** | Notion, Swagger, GitHub Projects |
| **Communication** | RESTful API, JSON |

---

## 🧩 API Summary

| 기능 | Method | Endpoint | 설명 |
|------|---------|-----------|------|
| 회원가입 / 로그인 | `POST` | `/signup`, `/login` | 사용자 인증 |
| 가족 그룹 생성 | `POST` | `/family-group` | 가족 그룹 신규 생성 |
| 가족 구성원 조회 | `GET` | `/family-group/{id}/members` | 멤버 목록 조회 |
| 자서전 조회 | `GET` | `/users/{id}/chapters` | 사용자별 자서전 목록 |
| 음성 답변 업로드 | `POST` | `/questions/{id}/answers` | Whisper-STT 기반 변환 |
| 자서전 페이지 조회 | `GET` | `/users/{id}/chapters/{chapterId}/pages` | 단락별 자서전 보기 |

---

## 📄 Architecture

```plaintext
[User Device]
   🎤 음성 녹음
       ↓ (파일 업로드)
[AWS S3]
       ↓
[Spring Boot Backend]
   ├── Whisper STT 요청
   ├── GPT-4 요약/감정 분석
   ├── Chapter/Question API
   └── PostgreSQL 저장
         ↓
[Client App]
   📖 요약된 스토리 / 감정 리포트 표시
