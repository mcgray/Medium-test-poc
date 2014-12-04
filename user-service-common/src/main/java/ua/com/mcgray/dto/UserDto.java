package ua.com.mcgray.dto;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.springframework.core.style.ToStringCreator;
import ua.com.mcgray.domain.User;

/**
 * @author orezchykov
 * @since 03.12.14
 */

public class UserDto implements Serializable {

    private Long id;

    private String emailAddress;

    private Long toDoShareAccountId;

    public UserDto(final User user) {
        this.id = user.getId();
        this.emailAddress = user.getEmailAddress();
        if (user.getToDoShareAccount() != null) {
            this.toDoShareAccountId = user.getToDoShareAccount().getId();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(final String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Long getToDoShareAccountId() {
        return toDoShareAccountId;
    }

    public void setToDoShareAccountId(final Long toDoShareAccountId) {
        this.toDoShareAccountId = toDoShareAccountId;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(this.id)
                .append(this.emailAddress)
                .append(this.toDoShareAccountId).toHashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UserDto other = (UserDto) obj;
        return new EqualsBuilder()
                .append(this.id, other.id)
                .append(this.emailAddress, other.emailAddress)
                .append(this.toDoShareAccountId, other.toDoShareAccountId).isEquals();
    }

    @Override
    public String toString() {
        return new ToStringCreator(this)
                .append("id", this.getId())
                .append("emailAddress", this.emailAddress)
                .append("toDoShareAccountId", this.toDoShareAccountId).toString();
    }
    
}
