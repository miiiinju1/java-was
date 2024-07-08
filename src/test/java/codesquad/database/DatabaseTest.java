package codesquad.database;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DatabaseTest {
    private Database<String> database;

    @BeforeEach
    void setUp() {
        database = new Database<>();
    }

    @DisplayName("Save: 일반적인 데이터 저장")
    @Test
    void saveGeneral() {
        // given
        String data = "data1";

        // when
        long id = database.save(data);

        // then
        assertThat(database.findById(id)).isPresent()
                .contains(data);
    }

    @DisplayName("Save: Null 데이터 저장 시 예외 발생")
    @Test
    void saveNull() {
        // given
        String data = null;

        // when & then
        assertThatThrownBy(() -> database.save(data))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("데이터가 null입니다.");
    }

    @DisplayName("Save: 여러 데이터 저장")
    @Test
    void saveMultiple() {
        // given
        String data1 = "data1";
        String data2 = "data2";

        // when
        long id1 = database.save(data1);
        long id2 = database.save(data2);

        // then
        assertThat(database.findById(id1)).isPresent()
                .contains(data1);
        assertThat(database.findById(id2)).isPresent()
                .contains(data2);
    }

    @DisplayName("Save: 경계값 테스트 (빈 문자열)")
    @Test
    void saveEmptyString() {
        // given
        String data = "";

        // when
        long id = database.save(data);

        // then
        assertThat(database.findById(id)).isPresent()
                .contains(data);
    }

    @DisplayName("Save: 중복 데이터 저장")
    @Test
    void saveDuplicate() {
        // given
        String data = "data1";

        // when
        database.save(data);
        long id = database.save(data);

        // then
        assertThat(database.findById(id)).isPresent()
                .contains(data);
    }

    @DisplayName("FindById: 일반적인 데이터 조회")
    @Test
    void findByIdGeneral() {
        // given
        String data = "data1";
        long id = database.save(data);

        // when
        Optional<String> retrievedData = database.findById(id);

        // then
        assertThat(retrievedData).isPresent()
                .contains(data);
    }

    @DisplayName("FindById: 존재하지 않는 ID 조회 시 비어있는 Optional 반환")
    @Test
    void findByIdNonExistent() {
        // given
        long nonExistentId = 999L;

        // when
        Optional<String> retrievedData = database.findById(nonExistentId);

        // then
        assertThat(retrievedData).isNotPresent();
    }

    @DisplayName("FindById: 여러 데이터 조회")
    @Test
    void findByIdMultiple() {
        // given
        String data1 = "data1";
        String data2 = "data2";
        long id1 = database.save(data1);
        long id2 = database.save(data2);

        // when
        Optional<String> retrievedData1 = database.findById(id1);
        Optional<String> retrievedData2 = database.findById(id2);

        // then
        assertThat(retrievedData1).isPresent()
                .contains(data1);
        assertThat(retrievedData2).isPresent()
                .contains(data2);
    }

    @DisplayName("FindById: 경계값 테스트 (ID 0)")
    @Test
    void findByIdZero() {
        // given
        long id = 0L;

        // when
        Optional<String> retrievedData = database.findById(id);

        // then
        assertThat(retrievedData).isNotPresent();
    }

    @DisplayName("Delete: 일반적인 데이터 삭제")
    @Test
    void deleteGeneral() {
        // given
        String data = "data1";
        long id = database.save(data);

        // when
        database.delete(id);

        // then
        assertThat(database.findById(id)).isNotPresent();
    }

    @DisplayName("Delete: 존재하지 않는 ID 삭제 시 예외 발생")
    @Test
    void deleteNonExistent() {
        // given
        long nonExistentId = 999L;

        // when & then
        assertThatThrownBy(() -> database.delete(nonExistentId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 id를 가진 데이터가 없습니다.");
    }

    @DisplayName("Delete: 여러 데이터 삭제")
    @Test
    void deleteMultiple() {
        // given
        String data1 = "data1";
        String data2 = "data2";
        long id1 = database.save(data1);
        long id2 = database.save(data2);

        // when
        database.delete(id1);
        database.delete(id2);

        // then
        assertThat(database.findById(id1)).isNotPresent();
        assertThat(database.findById(id2)).isNotPresent();
    }

    @DisplayName("Delete: 경계값 테스트 (ID 0)")
    @Test
    void deleteZero() {
        // given
        long id = 0L;

        // when & then
        assertThatThrownBy(() -> database.delete(id))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 id를 가진 데이터가 없습니다.");
    }

    @DisplayName("Delete: 삭제 후 재삽입")
    @Test
    void deleteAndReinsert() {
        // given
        String data = "data1";
        long id = database.save(data);

        // when
        database.delete(id);
        long newId = database.save(data);

        // then
        assertThat(database.findById(newId)).isPresent()
                .contains(data);
    }

    @DisplayName("Update: 일반적인 데이터 업데이트")
    @Test
    void updateGeneral() {
        // given
        String initialData = "data1";
        String updatedData = "data2";
        long id = database.save(initialData);

        // when
        database.update(id, updatedData);

        // then
        assertThat(database.findById(id)).isPresent()
                .contains(updatedData);
    }

    @DisplayName("Update: 존재하지 않는 ID 업데이트 시 예외 발생")
    @Test
    void updateNonExistent() {
        // given
        long nonExistentId = 999L;
        String updatedData = "data2";

        // when & then
        assertThatThrownBy(() -> database.update(nonExistentId, updatedData))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 id를 가진 데이터가 없습니다.");
    }

    @DisplayName("Update: Null 데이터 업데이트 시 예외 발생")
    @Test
    void updateNull() {
        // given
        String initialData = "data1";
        long id = database.save(initialData);
        String nullData = null;

        // when & then
        assertThatThrownBy(() -> database.update(id, nullData))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("데이터가 null입니다.");
    }

    @DisplayName("Update: 경계값 테스트 (빈 문자열 업데이트)")
    @Test
    void updateEmptyString() {
        // given
        String initialData = "data1";
        String emptyData = "";
        long id = database.save(initialData);

        // when
        database.update(id, emptyData);

        // then
        assertThat(database.findById(id)).isPresent()
                .contains(emptyData);
    }

    @DisplayName("Update: 동일 데이터로 업데이트")
    @Test
    void updateSameData() {
        // given
        String data = "data1";
        long id = database.save(data);

        // when
        database.update(id, data);

        // then
        assertThat(database.findById(id)).isPresent()
                .contains(data);
    }

    @DisplayName("FindAll: 모든 데이터 조회")
    @Test
    void findAll() {
        // given
        String data1 = "data1";
        String data2 = "data2";
        database.save(data1);
        database.save(data2);

        // when
        Collection<String> allData = database.findAll();

        // then
        assertThat(allData).containsExactlyInAnyOrder(data1, data2);
    }

    @DisplayName("FindByCondition: 조건에 맞는 데이터 조회")
    @Test
    void findByCondition() {
        // given
        String data1 = "data1";
        String data2 = "data2";
        database.save(data1);
        database.save(data2);

        // when
        Collection<String> filteredData = database.findAllByCondition(data -> data.startsWith("data1"));

        // then
        assertThat(filteredData).containsExactly(data1);
    }

    @DisplayName("FindByCondition: 조건에 맞는 데이터 하나 찾기")
    @Test
    void findByConditionSingleMatch() {
        // given
        String data1 = "data1";
        String data2 = "data2";
        database.save(data1);
        database.save(data2);

        // when
        Optional<String> result = database.findByCondition(data -> data.equals("data1"));

        // then
        assertThat(result).isPresent().contains(data1);
    }

    @DisplayName("FindByCondition: 조건에 맞는 데이터가 여러 개 존재할 때 예외 발생")
    @Test
    void findByConditionMultipleMatch() {
        // given
        String data1 = "data1";
        String data2 = "data1";
        database.save(data1);
        database.save(data2);

        // when & then
        assertThatThrownBy(() -> database.findByCondition(data -> data.equals("data1")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("조건에 맞는 데이터가 여러 개 존재합니다.");
    }

    @DisplayName("FindByCondition: 조건에 맞는 데이터가 없을 때")
    @Test
    void findByConditionNoMatch() {
        // given
        String data1 = "data1";
        String data2 = "data2";
        database.save(data1);
        database.save(data2);

        // when
        Optional<String> result = database.findByCondition(data -> data.equals("data3"));

        // then
        assertThat(result).isNotPresent();
    }
}
