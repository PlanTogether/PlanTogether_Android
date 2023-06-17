# PlanTogether_Android
플랜투게더 어플의 리포지토리입니다.

# Commit Message Convention
### 1. 기본 구조
> 제목 (Type: Subject)
> (한줄 띄고)
> 본문 (Body)
> (한줄 띄고)
> 꼬리말 (Footer)

### 2. 커밋의 타입 구성
> 태그: 제목
> : 뒤에만 space를 넣는다.

| Tag | Description |
| :---: | :---: | :---: |
| `Feat` | 새로운 기능을 추가 |
| `Fix` | 버그 수정 |
| `Design` | CSS 등 사용자 UI 디자인 변경 |
| `!BREAKING CHANGE` | 커다란 API 변경의 경우 |
| `!HOTFIX` | 급하게 치명적인 버그를 고쳐야하는 경우 |
| `Style` | 코드 포맷 변경, 세미 콜론 누락, 코드 수정이 없는 경우 |
| `Refactor` | 프로덕션 코드 리팩토링 |
| `Comment` | 필요한 주석 추가 및 변경 |
| `Docs` | 문서 수정 |
| `Test` | 테스트 코드, 리펙토링 테스트 코드 추가, Production Code(실제로 사용하는 코드) 변경 없음 |
| `Chore` | 빌드 업무 수정, 패키지 매니저 수정, 패키지 관리자 구성 등 업데이트, Production Code 변경 없음 |
| `Rename` | 파일 혹은 폴더명을 수정하거나 옮기는 작업만인 경우 |
| `Remove` | 파일을 삭제하는 작업만 수행한 경우 |

- 예시
  - Feat | Home - 서치바 레이아웃
  - Add | 리포트 에셋 추가

### 3. Subject
- 제목은 50글자 이내
- 첫글자는 대문자
- 마침표 및 특수기호 사용X
- 현재시제
- 간결하게

### 4. Body
- 72자 이내
- 최대한 상세히(코드 변경의 이유가 명확할수록 좋음)
- 어떻게보다 무엇을, 왜

이슈 번호는 사용하지 않아도 괜찮습니다 !
