generate-cards.py works by reading from a deck design directory and generating 
individual SVG files from the information retrieved. It also exports each SVG 
file to a PNG file.

Its design concept is based on the simple fact that each playing card has four 
recurring components: the background, the number on the corners, the suit symbol
on the corners, and the content.

The content is another recurring pattern: except for ace of spades and the jack,
king, and queen in each suit, same numbers across each suit bear the same 
patterns. For example, a four, no matter what suit it belongs to, consists of 
two upright suit symbols at the top left and top right corners respectively, and
two upside-down suit symbols at the bottom left and bottom right corners 
respectively. A five, is like a four, but with an additional upright suit symbol
centered horizontally and vertically in the middle. A pattern is defined as a 
set of coordinates that each suit symbol bears. The origin of the coordinates 
are at the top left.

Based on these simple facts, one can design SVG files conforming to the required
specifications and place them in a directory conforming to the specification to 
form a deck design.

A deck design is a directory of the following structure:

* pattern-generator.rb
* card-face.svg
* number.svg
* suit.svg
* bounds.txt
* colors.txt
* suits/
|--* clubs.svg
|--* clubs-flipped.svg
|--* diamonds.svg
|--* diamonds-flipped.svg
|--* hearts.svg
|--* hearts-flipped.svg
|--* spades.svg
|--* spades-flipped.svg
* overrides/
|--* clubs-J.svg
|--* clubs-Q.svg
|--* clubs-K.svg
|--* diamonds-J.svg
|--* diamonds-Q.svg
|--* diamonds-K.svg
|--* hearts-J.svg
|--* hearts-Q.svg
|--* hearts-K.svg
|--* spades-J.svg
|--* spades-Q.svg
|--* spades-K.svg

pattern-generator.rb
--------------------
pattern-generator.rb is a Ruby script that is responsible for generating the 
coordinates for a pattern. For most Anglo-Hispanic-French card suits, no custom 
pattern-generator.rb is needed. A copy from dorian/pattern-generator.rb would 
do. Should a custom one be needed, the custom script should conform to the 
following specifications.

It must function as a shell script and accept two arguments. The first argument 
is a string, denoting the numeric value of the pattern generated. The second 
argument is an optional string that can be evaluated into a Ruby hash. The Ruby 
hash can have the following entries, each entry dictating an option:
* :width - The width of the boundary.
* :height - The height of the boundary.
* :w - The width of the suit symbol.
* :h - The height of the suit symbol.
* :x - The offset x value of the boundary.
* :y - The offset y value of the boundary.
Each entry must have a Ruby symbol as the key and a float number as the value. 
All entries are optional. When entries are missing, the script must use an 
arbitrary default value. The recommended default values of :x and :y are both 
0.0. Here is a sample string that can be evaluated into a Ruby hash that 
conforms to the above specification:

	"{ :width => 147.0, :w => 26.894531, :h => 36.855469, :height => 237.0 }"

The generated output from the script must be directed to STDOUT. The Ruby method
puts writes to STDOUT. The output must be a string representation of a list of 
lists. A list is the Python equivalent of a Ruby array; both bear identical 
syntax. A string representation of a Ruby array can be obtaining by calling the  
inspect method of the array. Each list item is a list with three elements: the 
x coordinate in float, the y coordinate in float, and whether the suit symbol 
should be flipped in boolean value. Note that in Python, boolean literals are 
capitalized, while in Ruby they are in all lower cases. Here is a sample Ruby 
snippet that will output a string representation of a list of lists that 
conforms to the above specification.

    result = [[112.5, 0.0, false], [112.5, 157.5, false], [112.5, 315.0, true]]
    puts result.inspect.gsub("true", "True").gsub("false", "False")

card-face.svg
-------------
card-face.svg is used as the "base" for every card. Think of it as a background 
for every possible card. As such, there are few limitations of the content of 
card-face.svg, other than that its document dimensions would serve as the 
template dimensions for all other SVG files.

It is recommended that all other SVG files in a deck design to have the same 
dimensions as card-face.svg.

number.svg
----------
number.svg is the corner overlay that holds the numeric value of a card. The 
value can be one of the following: A, 2, 3, 4, 5, 6, 7, 8, 9, 10, J, Q, or K. 
Usually, in the United States, they are on the top left and bottom right corner.

Most SVG editors would render a piece of text as something akin to:

    <text id="blah"><tspan id="bleh">text</tspan></text>

There would be, for most of the time, two pieces of text. For example, there can
be an upright piece of text on the top left corner and an upside-down piece of 
text on the bottom right corner.

The content (i.e. the actual text) of the <tspan> elements does not matter, 
although a placeholder "n" is recommended to be the content. At runtime, 
generate-cards.py will replace "n" or whatever the content of the element might 
be to the corresponding numeric value of the current card.

The color of the <tspan> element also does not matter. It will be replaced with 
the ones specified in colors.txt, or, if colors.txt doesn't exist, a default 
color specified by generate-cards.py.

suit.svg
--------
suit.svg is the corner overlay that holds the suit symbol of a card. The symbol 
is usually among the four following Unicode codepoints: U+2665 (hearts), U+2666 
(diamonds), U+2663 (clubs), or U+2660 (spades). This is not at all enforced; the
actual Unicode strings that will be utilized are dictated by the SVG files in 
the suits/ directory.

Like number.svg, there are usually two pieces of text, one upright, one upside-
down, in a corner overlay. Likewise, the content of the <tspan> elements does 
not matter, although a placeholder "x" is recommended to be the content. At 
runtime, generate-cards.py will replace "x" or whatever the content of the 
element might be to the corresponding Unicode string for the suit of the current
card.

By extension, the color of the <tspan> element does not matter because
generate-cards.py will take care of it.

The text-oriented nature of this current mechanism for both number.svg and 
suit.svg implies that custom suit symbols (for example, of other cultures) 
are currently not possible to be used unless they have a place in the Unicode 
plane. For example, German suits do not consist of hearts, diamonds, clubs, and 
spades, but Herz (hearts), Schellen (bells), Eichel (acorns), and Laub (leaves).

There is room for future improvement.

bounds.txt
----------
bounds.txt contains the boundaries that patterns are to be fitted into. Its 
syntax is a JSON-like object, but more accurately described as a Python 
dictionary. The syntax is also less strict in a sense that Python expressions 
are allowed, so calling the sum() function or performing division is perfectly 
acceptable.

There are four properties:
* x - The offset x value of the boundary.
* y - The offset y value of the boundary.
* width - The width of the boundary.
* height - The height of the boundary.

It is required that all four properties be floats, thus even if you have an 
integer value (e.g. 40), you must syntactically represent it as a float value 
(e.g. 40.0).

colors.txt
----------
colors.txt contains the colors that each suit is assigned to. Its syntax is just
like the syntax of bounds.txt.

There are four properties:
* hearts - The color of hearts.
* diamonds - The color of diamonds.
* clubs - The color of clubs.
* spades - The color of spades.

Each color is represented by a string containing a hexadecimal HTML color (e.g. 
#FF0000).

suits
-----
suits is a directory containing the SVG files for suit symbols that are to be 
used to generate patterns. For each suit, an upright version and an upside-down 
version must be provided. For example, hearts.svg would be the file for hearts, 
while hearts-flipped.svg would be the file for upside-down hearts.

For both files, the symbol in the SVG must be aligned to the top left corner of 
the document. For both files, the document dimensions must match those of 
card-face.svg.

Currently, generate-cards.py assumes the symbols in the SVG files to be always 
a piece of <text>. There is room for improvement.

overrides
---------
overrides is a directory containing SVG files for cards that do not normally 
adhere to patterning rules. For example, the jacks, kings, and queens for each 
suit almost always have their own elaborate designs. The ace of spades also 
commonly has its own elaborate design.

generate-cards.py does not enforce ace of spades to have an elaborate design of 
its own, but for the jacks, kings, and queens, there must be overrides. The 
filename of an override is in the following format:

    suit-number.svg

where suit is the suit name and number is a value from A to K.

SVG files for overrides are recommended to have the same dimensions as those of 
card-face.svg, but this is not enforced, as all layers of overrides are simply 
overlaid on top of cards.

Generated Files
---------------
generate-cards.py generates the overlaid SVG files in the generated-svgs/ 
directory of the deck design directory. Likewise, PNG files are generated in the
generated-pngs/ directory. If the target directories do not exist, they are 
first created.

TODOS
-----
One future plan for generate-cards.py is that there should not be a limited 
number of suits (some cultures have five suits, for example), and the names of 
the suits should not be enforced.
