package seedu.address.logic.commands;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Comment;
import seedu.address.model.person.Person;

/**
 * Changes the comment of an existing person identified using the displayed index from the address book.
 */
public class CommentCommand extends Command {

    public static final String COMMAND_WORD = "comment";
    public static final String MESSAGE_ADD_COMMENT_SUCCESS = "Added comment to Person: %1$s";
    public static final String MESSAGE_DELETE_COMMENT_SUCCESS = "Removed comment from Person: %1$s";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Edits the comment of the person identified "
            + "by the index number used in the last person listing. "
            + "Existing comment will be overwritten by the input.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "c/ [COMMENT]\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + "c/ Likes to swim.";

    public static final String MESSAGE_ARGUMENTS = " Index: %1$d, Comment: %2$s";

    private final Index index;
    private final Comment comment;

    /**
     * @param index of the person in the filtered person list to edit the comment
     * @param comment of the person to be updated to
     */
    public CommentCommand(Index index, Comment comment) {
        requireAllNonNull(index, comment);

        this.index = index;
        this.comment = comment;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());
        Person editedPerson = new Person(
                personToEdit.getName(), personToEdit.getPhone(), personToEdit.getEmail(),
                personToEdit.getAddress(), comment, personToEdit.getTags(), personToEdit.isVip());

        model.setPerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        return new CommandResult(generateSuccessMessage(editedPerson));
    }

    /**
     * Generates a command execution success message based on whether
     * the remark is added to or removed from
     * {@code personToEdit}.
     */
    private String generateSuccessMessage(Person personToEdit) {
        String message = !comment.fullComment.isEmpty() ? MESSAGE_ADD_COMMENT_SUCCESS : MESSAGE_DELETE_COMMENT_SUCCESS;
        return String.format(message, Messages.format(personToEdit));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof CommentCommand)) {
            return false;
        }

        CommentCommand e = (CommentCommand) other;
        return index.equals(e.index)
                && comment.equals(e.comment);
    }
}
