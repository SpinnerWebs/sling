# Apache Sling UUIDResourceUseProvider
A UseProvider that looks up JCR resources by UUID.
## Why is a UUIDResourceUseProvider useful?
JCR allows a node to use the [mix:referenceable] mixin, which automatically assigns the node a UUID to uniquely identify it in the tree.
However, there is no convenient way for the Sightly/HTL language to look up a node by this UUID.

The UUIDResourceUseProvider therefore allows easy access to a resource via its UUID through the HTL data-sly-use statement.
