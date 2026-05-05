package exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ItemLimitReachedException extends RuntimeException {
    public ItemLimitReachedException(String message) {
        super(message);
    }
}
