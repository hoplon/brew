# Brew
> Let a man walk ten miles steadily on a hot summer's day along a dusty English road, and he will soon discover why beer was invented. [Gilbert K. Chesterton](http://www.brainyquote.com/quotes/quotes/g/gilbertkc553977.html?src=t_beer)

[](dependency)
```clojure
[hoplon/brew "0.3.0"] ;; latest release
```
[](/dependency)

----------
Experimental brewing for Hoplon components.

Brew provides experimental community contrib features. The things you see
here may:

* be moved into future versions of `hoplon`,
* remain in `brew` to avoid scope creep,
* vanish into thin air. :sparkles:

Brew is structured as a individual `hops`. A hop is a (generally) selfcontained
feature or library interop in the form of a namespace ex. `hoplon.bidi`.
Hops may burst into :fire:.

### Community Support
Brew contains mostly incubating projects and support is available from the community.
Check us out on [#slack](https://clojurians.slack.com/messages/hoplon/)


### Documentation
May be:
* limited
* non-existant
* or wrong.

## Hacking

```
# build and install locally
boot develop
```

## License

```
Copyright (c) Hoplon Contributors. All rights reserved.

The use and distribution terms for this software are covered by the Eclipse
Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php) which can
be found in the file epl-v10.html at the root of this distribution. By using
this software in any fashion, you are agreeing to be bound by the terms of
this license. You must not remove this notice, or any other, from this software.
```
