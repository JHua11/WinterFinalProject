package exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ItemUnavailableException extends RuntimeException {
    public ItemUnavailableException(String message) {
        super(message);
    }
}
