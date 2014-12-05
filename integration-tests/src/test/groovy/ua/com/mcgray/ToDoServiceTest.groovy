package ua.com.mcgray
import com.jayway.jsonpath.JsonPath
import org.springframework.http.HttpMethod
import spock.lang.Shared
import spock.lang.Specification
import ua.com.mcgray.utils.RestCaller
/**
 * @author orezchykov
 * @since 05.12.14
 */

class ToDoServiceTest extends Specification {

    @Shared
    def restCaller = new RestCaller(8800, "todoService")

    def "should get a list of ToDos by user Id using a Hessian call"() {
        given: "user id equals 1"
            def userId = 1

        when:  "when I call ToDO service"

            def result = restCaller.queryApi(HttpMethod.GET, "/remoting/api/todo/" + userId)

        then: "then I get the ToDo list for the user"
        assert result
        def readContext = JsonPath.parse(result.getBody())

        assert readContext.read("\$.[0].title") == "User personal ToDo"

    }





}
