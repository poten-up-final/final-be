# n8n 워크플로우 제작 표준 작업 절차서 (SOP)

## 1. 프로젝트 개요

- **프로젝트명**: 전신 코디(Outfit) 이미지 자동 필터링 워크플로우
- **목적**: 크롤링된 이미지가 사람이 직접 착용한 '코디 샷'인지 판별하고, 단일 아이템(가방, 화장품, 바닥 컷 등) 사진을 걸러내어 백엔드로 결과를 반환하는 자동화 파이프라인 구축.

---

## 2. 워크플로우 아키텍처 및 노드 구성 상세

본 워크플로우는 크롤링된 이미지 데이터를 백엔드 시스템으로부터 수신받아, 비전 AI 모델을 통해 인물 착용 샷 여부를 판별한 후 그 결과를 백엔드 콜백 API로 다시 전달합니다.

### 2.1. 트리거 (Webhook Node)
백엔드(Spring Boot) 시스템으로부터 이미지 검수 요청을 수신하는 진입점입니다.

- **Node Type**: Webhook
- **Method**: `POST`
- **수신 Payload 데이터 구조**:
  ```json
  {
    "cardImageId": "고유 식별자 (String/Number)",
    "imageUrl": "검수할 상품 이미지의 URL 주소",
    "productTitle": "해당 상품의 이름/타이틀 (판별 참고용)"
  }
  ```

### 2.2. AI 처리 (Google Gemini Node)
Webhook으로 수신된 이미지 URL과 참고용 텍스트를 기반으로 코디 샷 여부를 판별합니다.

- **Node Type**: Google Gemini
- **Model**: Gemini 2.0 Flash (비전 및 텍스트 처리에 최적화)
- **Operation**: Analyze an image
- **Input Configuration**:
  - Image: `HTTP Request` 노드에서 다운로드한 바이너리 파일 (`data`)
  - Text: Webhook 노드에서 수신한 참고용 `productTitle`
- **Prompt (System/User Instruction)**:
  > 너는 패션 매거진의 매우 엄격한 이미지 검수자야. 
  > 1. 사진에 실제 '사람(모델)'이 보이지 않으면 무조건 'AI_FLAGGED'야. 
  > 2. 신발, 가방, 옷만 덩그러니 놓여 있는 '바닥 컷'이나 '제품 단독 샷'은 절대로 통과시키지 마. 
  > 3. 오직 사람이 직접 옷을 입고 있는 '전신' 또는 '착용 모습'이 보일 때만 'AI_PASSED'로 설정해.
  > 결과를 아래 JSON 형식으로만 답해:
  > {"status": "AI_PASSED" 또는 "AI_FLAGGED", "aiComment": "이유(50자 이내)"}

### 2.3. 결과 반환 (Set Result / HTTP Request Node)
Gemini 모델의 이미지 필터링 결과를 백엔드 시스템으로 다시 전달합니다.

- **Node Type**: Set (Result Parsing)
- **추출 필드**:
  ```json
  {
    "cardImageId": "{{ Webhook에서 받은 원본 cardImageId }}",
    "status": "{{ Gemini가 판별한 상태값 (AI_PASSED 또는 AI_FLAGGED) }}",
    "aiComment": "{{ Gemini가 작성한 판별 사유 }}"
  }
  ```

---

## 3. 예외 처리 및 로깅 정책 (Error Handling)

외부 AI API 호출은 네트워크 지연이나 서비스 장애에 민감하므로 견고한 예외 처리가 필수적입니다.

- **Retry 메커니즘 설정**:
  - 대상 노드: Google Gemini Node (AI 처리)
  - 조건: API 호출 실패(5xx 에러) 또는 Rate Limit(429) 발생 시
  - 설정값: **최대 5회 재시도 (Wait Between Tries: 5000ms)**
- **로깅 및 에러 대응**:
  - URL 공백/마점표 등으로 인한 에러 방지를 위해 `.trim()` 처리 필수 적용.
