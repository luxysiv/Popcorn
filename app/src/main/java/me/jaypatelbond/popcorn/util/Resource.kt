package me.jaypatelbond.popcorn.util

/**
 * A sealed class that encapsulates successful outcome with a value of type [T]
 * or a failure with an error message and optional data.
 */
sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    /**
     * Represents a successful state with data.
     */
    class Success<T>(data: T) : Resource<T>(data)

    /**
     * Represents an error state with an optional message and cached data.
     */
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)

    /**
     * Represents a loading state with optional cached data.
     */
    class Loading<T>(data: T? = null) : Resource<T>(data)
}
