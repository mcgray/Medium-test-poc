package ua.com.mcgray.domain;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author orezchykov
 * @since 10.02.15
 */
public class ToDoShareAccountDto implements Serializable {

    private final Long id;

    private final String nickname;

    private final String firstName;

    private final String lastName;

    public ToDoShareAccountDto(ToDoShareAccount toDoShareAccount) {
        this.id = toDoShareAccount.getId();
        this.nickname = toDoShareAccount.getNickname();
        this.firstName = toDoShareAccount.getFirstName();
        this.lastName = toDoShareAccount.getLastName();
    }

    public Long getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        ToDoShareAccountDto rhs = (ToDoShareAccountDto) obj;
        return new EqualsBuilder()
                .append(this.id, rhs.id)
                .append(this.nickname, rhs.nickname)
                .append(this.firstName, rhs.firstName)
                .append(this.lastName, rhs.lastName)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(id)
                .append(nickname)
                .append(firstName)
                .append(lastName)
                .toHashCode();
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("nickname", nickname)
                .append("firstName", firstName)
                .append("lastName", lastName)
                .toString();
    }
}
