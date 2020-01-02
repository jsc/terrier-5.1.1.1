This is a fork of Terrier 5.1. Several small changes have been made to the
code for experimental purposes. These are:
* Addition of a simple "S" stemmer as done by Atire.
* Addition of Krovetz stemmer.
* Addition of Lovetiz stemmer.
* Modified BM25 to use Atire weightings.
* Rewrote DirichletLM to be more faithful to the Indri version of QL.

In order to distinguish this fork from the original release, I have bumped
the release up to 5.1.1.1. This is not an official Terrier release. Please
see README-5.1.md for Glasgow details or go to https://terrier.org if you
are looking for the original.
