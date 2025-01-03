package startoy.puzzletime.service;

import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import startoy.puzzletime.dto.puzzle.GetPuzzleResponse;
import startoy.puzzletime.repository.PuzzlePlayRepository;

@Service
@RequiredArgsConstructor
public class PuzzleService {

    private final PuzzlePlayRepository puzzlePlayRepository;

    public GetPuzzleResponse getPuzzleByUid(String puzzleUid, String userId) {
        validatePuzzleUid(puzzleUid);

        // 쿼리 결과 조회
        Map<String, Object> result = puzzlePlayRepository.findByPuzzleUidAndUserId(puzzleUid, userId);

        // 결과 검증
        if (result == null || result.isEmpty()) {
            throw new IllegalArgumentException("No puzzle play data found for puzzleUid: " + puzzleUid);
        }

        // 필수 필드 검증
        validateRequiredFields(result, "puzzleUid", "puzzleImageUrl");

        // puzzlePlayUid 없으면
        String puzzlePlayUid = result.get("puzzlePlayUid").toString();
        if (puzzlePlayUid.isEmpty()) {
            // 신규 UID 생성
            puzzlePlayUid = UUID.randomUUID().toString();
        }

        // 값 추출 및 DTO 생성
        return GetPuzzleResponse.builder()
                .puzzleUid(getString(result, "puzzleUid"))
                .ImageUrl(getString(result, "puzzleImageUrl"))
                .puzzlePlayUid(puzzlePlayUid)
                .puzzlePlayData(getString(result, "puzzlePlayData"))
                .build();
    }

    // 필수 필드 검증 메서드
    private void validatePuzzleUid(String puzzleUid) {
        if (puzzleUid == null || puzzleUid.isEmpty()) {
            throw new IllegalArgumentException("puzzleUid is required");
        }
    }

    private void validateRequiredFields(Map<String, Object> result, String... fields) {
        for (String field : fields) {
            if (result.get(field) == null || result.get(field).toString().isEmpty()) {
                throw new IllegalArgumentException(field + " is not found or empty");
            }
        }
    }

    // Map에서 String 값 안전하게 추출
    private String getString(Map<String, Object> map, String key) {
        return Optional.ofNullable(map.get(key)).map(Object::toString).orElse(null);
    }

    // Map에서 Boolean 값 안전하게 추출
    private boolean getBoolean(Map<String, Object> map, String key) {
        return Optional.ofNullable((Boolean) map.get(key)).orElse(false);
    }

    // Map에서 Integer 값 안전하게 추출
    private int getInt(Map<String, Object> map, String key) {
        return Optional.ofNullable((Number) map.get(key)).map(Number::intValue).orElse(0);
    }
}
