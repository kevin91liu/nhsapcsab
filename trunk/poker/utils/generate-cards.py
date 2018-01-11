#!/usr/bin/env python
# encoding: utf-8
"""
generate-cards.py

Created by kourge on 2008-06-13.
Word of warning:
If you value your sanity, don't read this.
If you think you can cope with it, go ahead.

Generates a deck of cards from a directory that conforms to specification.
Please refer to README.txt in dorian/ for more information on the specs.
"""

import sys
import os
import commands
from xml.dom.minidom import *

class Inkscape(object):
  """Provides Inkscape services such as SVG info querying and exporting."""
  @staticmethod
  def run(args):
    """Runs Inkscape with specified args and returns result.
    args can be a dict or a string.
    """
    path = "/Applications/Inkscape.app/Contents/Resources/bin/inkscape"
    delimiter = "Using the fallback 'C' locale.\n"
    if args.__class__ is dict:
      args = " ".join(["--%s=%s" % (k, v) for k, v in args.items()])
    return commands.getoutput(path + " " + args).split(delimiter)[-1]
  
  @staticmethod
  def query(info, file):
    """Query an SVG file for info. info can be x, y, height, or width."""
    return float(Inkscape.run('--without-gui --query-%s %s' % (info, file)))
  
  @staticmethod
  def export(format, source, dest, extraparams={}):
    """Export source SVG file to dest path in format, optionally with a specified width and height."""
    params = {"file": source, "export-%s" % format: dest}
    for k, v in extraparams.items():
      params["export-%s" % k] = v
    return Inkscape.run(params)


def main():
  path = os.path.expanduser(sys.argv[1])
  symgen = os.path.join(path, "pattern-generator.rb")
  # Create target directories if they don't exist yet.
  target_dirs = ["generated-svgs", "generated-pngs"]
  for dir in [os.path.join(path, x) for x in target_dirs]:
    if not os.path.exists(dir):
      os.mkdir(dir)
  # Component filenames
  bases = dict(card="card-face.svg", suit="suit.svg", number="number.svg")
  # Populate parameters
  params = eval(open(os.path.join(path, "bounds.txt")).read())
  colors = eval(open(os.path.join(path, "colors.txt")).read())
  # Iterate through each suit
  for suit in colors.keys():
    suitfile = os.path.join(path, "suits", "%s.svg" % suit)
    param = params.copy()
    # Fill in suit-specific dimensions
    param.update({
      "w": Inkscape.query("width", suitfile),
      "h": Inkscape.query("height", suitfile)
    })
    # Open corresponding files
    svgM = open(suitfile)
    svgW = open(os.path.join(path, "suits", "%s-flipped.svg" % suit))
    docs = [open(os.path.join(path, bases[x])) for x in ["card", "suit", "number"]]
    # Iterate through each number
    for number in ["A"] + [str(x) for x in range(2, 11)] + ["J", "Q", "K"]:
      svgs = [parse(x) for x in docs]
      if number not in ["J", "Q", "K"] and \
         not os.path.exists( \
           os.path.join(path, "overrides", "%s-%s.svg" % (suit, number)) \
         ):
        # Call pattern generator for coordinates
        command = '%s %s "%s"' % (symgen, number, rubify(param))
        for x, y, flipped in eval(commands.getoutput(command)):
          # Normal or flipped?
          svg = parse(flipped and svgW or svgM)
          node = svg.getElementsByTagName("text")[0]
          # Translate symbol to the coordinate it belongs to
          transform = node.getAttribute("transform")
          if transform != "": transform += " "
          transform += "translate(%s, %s)" % (x, (flipped and -y or y))
          # Set the transformation
          node.setAttribute("transform", transform)
          # Add to the list of overlays
          svgs.append(svg)
          # Clean up and reposition file pointer
          svgM.seek(0); svgW.seek(0)
      else:
        # Open override file
        override = os.path.join(path, "overrides", "%s-%s.svg" % (suit, number))
        override = open(override)
        # Add to the list of overlays and clean up
        svgs.append(parse(override)); override.close()
      # Prepare suit corner and number corner overlays
      suitsvg = svgs[1]; numbersvg = svgs[2]
      # Use the appropriate color
      color = colors[suit]
      # Prepare symbol unicode character
      svgM.seek(0)
      blah = parse(svgM)
      symbol = blah.getElementsByTagName("tspan")[0].childNodes[0].data
      # Process suit corner overlays
      suittexts = suitsvg.getElementsByTagName("text")
      suittspans = suitsvg.getElementsByTagName("tspan")
      # Replace suit unicode character
      suittspans = [x for x in suittspans if len(x.childNodes) != 0]
      for node in [x.childNodes[0] for x in suittspans]: node.data = symbol
      # Process number corner overlays
      numbertexts = numbersvg.getElementsByTagName("text")
      numbertspans = numbersvg.getElementsByTagName("tspan")
      # Replace number unicode character
      numbertspans = [x for x in numbertspans if len(x.childNodes) != 0]
      for node in [x.childNodes[0] for x in numbertspans]:
        node.data = str(number)
      # Change color accordingly
      all = suittexts[:] + suittspans + numbertexts + numbertspans
      for x in all: setStyle(x, {"fill": color})
      # Combine all overlays
      svgs = combine(svgs)
      # Write 'em out
      n = (number == "A") and "1" or number
      result = os.path.join(path, "generated-svgs", "%s-%s.svg" % (suit, n))
      png = os.path.join(path, "generated-pngs", "%s-%s.png" % (suit, n))
      output = open(result, "w")
      output.write(svgs.toxml().encode("utf-8"))
      output.close()
      Inkscape.export("png", result, png, {"dpi": 120})
      print "Generated %s of %s" % (number, suit)
      # Reposition file pointers
      for x in [svgM, svgW] + docs: x.seek(0)
    # Clean up all file handlers
    for x in docs + [svgM, svgW]: x.close()
  # Export card backs
  Inkscape.export("png", os.path.join(path, "card-back.svg"), \
                         os.path.join(path, "card-back.png"), {"dpi": 120})
  print "Generated card back"
  

def combine(documents):
  """Combines a list of DOM SVG Documents."""
  inkscapeNS = "http://www.inkscape.org/namespaces/inkscape"
  first = documents[0]
  layers = len(first.getElementsByTagName("g"))
  results = []
  for doc in documents[1:]:
    svglayers = doc.getElementsByTagName("svg")[0].childNodes
    nodes = [x for x in svglayers if x.nodeType == Document.ELEMENT_NODE]
    nodes = [first.importNode(x, True) for x in nodes if x.tagName == u"g"]
    results += nodes
  for n in range(layers, len(results) + layers + 1):
    node = results[n - 2]
    node.setAttribute("id", "layer%s" % n)
    node.setAttributeNS(inkscapeNS, "label", "Layer %s" % n)
    first.getElementsByTagName("svg")[0].appendChild(node)
  # TODO: Uniqify all ids of nodes who are children of <g>s
  # It seems like unique ids aren't required.
  return first

def css(css):
  """Takes a CSS string and returns a dict repr of it."""
  d = {}
  for entry in css.split(";"):
    k, v = entry.split(":"); d[k] = v
  return d

def decss(css):
  """Takes a CSS dict repr and returns it as a CSS string."""
  return ";".join(["%s:%s" % (k, v) for k, v in css.items()])

def setStyle(node, style):
  """Updates a node's style attribute with a CSS dict repr."""
  d = css(node.getAttribute("style"))
  d.update(style)
  node.setAttribute("style", decss(d))

def rubify(x):
  """Takes a dict and turns it into a Ruby hash repr."""
  return "{ %s }" % ", ".join([":%s => %s" % (k, v) for k, v in x.items()])

if __name__ == '__main__':
  main()

