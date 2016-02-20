package ua.com.mcgray
import com.jayway.jsonpath.JsonPath
import org.junit.experimental.categories.Category
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import spock.lang.Shared
import spock.lang.Specification
import ua.com.mcgray.test.LargeTest
import ua.com.mcgray.utils.RestCaller

/**
 * @author orezchykov
 * @since 05.12.14
 */

@Category(LargeTest.class)
class ToDoServiceTest extends Specification {

    @Shared
    def restCaller = new RestCaller(8800, "todoService")

    def "should get a list of ToDos by user Id using a Rest call"() {
        given: "user id equals 1"
            def userId = 1

        when:  "I call ToDO service"

            def result = restCaller.queryApi(HttpMethod.GET, "/todo/" + userId)

        then: "then I get the ToDo list for the user"
        assert result
        def readContext = JsonPath.parse(result.getBody())

        assert readContext.read("\$.[0].title") == "User personal ToDo"

    }

    def "should get exception when there is no user with such id"() {

        given: "a fake user id 123"

            def userId = 123

        when: "I call ToDo Service"

            def result = restCaller.queryApi(HttpMethod.GET, "/todo/" + userId)

        then: "then I get the exception"

        assert result
        assert result.getStatusCode() == HttpStatus.BAD_REQUEST

    }





}
