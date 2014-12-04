package ua.com.mcgray.domain;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.springframework.core.style.ToStringCreator;

/**
 * @author orezchykov
 * @since 04.12.14
 */

public class ToDoDto implements Serializable {

    private final Long id;
    private final String title;
    private final String note;
    private final boolean done;

    public ToDoDto(ToDo toDo) {
        this.id = toDo.getId();
        this.title = toDo.getTitle();
        this.note = toDo.getNote();
        this.done = toDo.isDone();

    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getNote() {
        return note;
    }

    public boolean isDone() {
        return done;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(this.done)
                .append(this.title)
                .append(this.note).toHashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ToDoDto other = (ToDoDto) obj;
        return new EqualsBuilder().appendSuper(super.equals(obj))
                .append(this.done, other.done)
                .append(this.title, other.title)
                .append(this.note, other.note).isEquals();
    }

    @Override
    public String toString() {
        return new ToStringCreator(this)
                .append("id", this.getId())
                .append("done", this.done)
                .append("title", this.title)
                .append("note", this.note).toString();
    }


}
