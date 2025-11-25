package io.github.angleto.fractran

/**
 * Exception thrown when FRACTRAN encounters an invalid input or state.
 */
case class FractranException(
  message: String = "",
  cause: Throwable = None.orNull
) extends Exception(message, cause)