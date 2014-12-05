package ua.com.mcgray

import spock.lang.Specification
import ua.com.mcgray.test.LargeTest

/**
 * @author orezchykov
 * @since 05.12.14
 */

@Category(LargeTest)
class ToDoServiceTest extends Specification {


    def 'should get a list of ToDos by user Id using a Hessian call'() {
        given:
        when:
        then:
        assert false
    }





}
