# Inpeak – 개발자 AI 면접 준비 플랫폼

Inpeak은 개발자 취업을 준비하는 사람들을 위한 **AI 기반 모의 면접 플랫폼**입니다.  
음성/영상으로 답변을 녹음하면 AI가 피드백을 제공하고, 실전처럼 면접 경험을 쌓을 수 있습니다.

<img width="1920" alt="스크린샷 2025-07-08 오전 1 43 20" src="https://github.com/user-attachments/assets/f8c51495-18ec-4bfe-b2af-f02129a6e223" />


서비스 링크 - https://inpeak.kr

---

## 주요 기능

원하는 기술 스택을 선택하고 해당 스택에 대한 면접 질문을 제공합니다. 사용자는 영상을 녹화하면서 (선택) 음성으로 면접에 답변을 할 수 있고,
답변한 내용은 Presigned URL 방식으로 S3에 올라갑니다. 이후 비동기 처리를 통해 Whisper로 음성 데이터를 텍스트로 변환한 다음 GPT 4o-mini
모델을 통해 피드백을 제공합니다. 사용자는 면접이 끝나고 본인의 답변 내용과 피드백 내용을 히스토리를 통해 확인할 수 있습니다.

---

## 👥 팀 소개

- **개발 기간**: 2025.02 ~ 2025.07
- **팀원 구성**: 총 3명 (백엔드 기준)
  - BE: 이민형 (팀장), 이지영, 강민우

---

## ⚙️ 기술 스택

### Backend
- Java 17, Spring Boot 3.x, Spring Data JPA, Spring Security
- JWT, OAuth2, Redis, Kafka, MySQL, queryDSL

### Infra & Tools
- AWS EC2, S3, Nginx
- Docker, GitHub Actions (CI)
- Notion, Figma, ERDCloud, Discord

---

## 🧱 시스템 아키텍처

<img width="1015" alt="스크린샷 2025-07-04 오후 8 33 59" src="https://github.com/user-attachments/assets/e5ec9aac-b2d4-4a66-b307-8be99449ce8b" />

## 🔗 ERD

<img width="782" alt="스크린샷 2025-07-08 오전 1 42 19" src="https://github.com/user-attachments/assets/b5e6d9bb-ebe3-4b89-93b4-20fd0692418a" />


## 🧠 담당 업무 (My Contribution – 이민형)

- Kafka + Whisper + GPT 연동 처리
- Redis를 통한 캐싱 및 인증 관리
- 히스토리 제공을 위한 queryDSL 동적 쿼리
- AOP를 활용한 공통 로깅 기능
- S3를 활용한 데이터 업로드, 다운로드

---
