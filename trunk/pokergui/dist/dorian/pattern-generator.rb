#!/usr/bin/env ruby

# The origin (0, 0) is at the top left
# An object's anchor is also at the top left

class Ace
  def initialize(*args)
    params = args[0].nil? ? {} : args[0]
    @width = params[:width] || 225.0; @height = params[:height] || 315.0
    @w = params[:w] || 0.0; @h = params[:h] || 0.0
  end
  attr_accessor :w, :h, :x, :y
  
  def each(&block) (0...length).each { |n| yield self[n] } end
  include Enumerable
  
  def [](i) send "s#{i + 1}" end
  def length()
    (1..11).each { |n| return (n - 1) if not respond_to? :"s#{n}" }
    0
  end
  
  def s1() [@width / 2 - @w / 2, @height / 2 - @h / 2, :M] end
end

class Two < Ace
  def s1() [@width / 2 - @w / 2, 0, :M] end
  def s2() [@width / 2 - @w / 2, @height - @h, :W] end
end

class Three < Two
  alias :s3 :s2
  define_method :s2, Ace.instance_method(:s1)
end

class Four < Ace
  def s1() [0, 0, :M] end
  def s2() [@width - @w, 0, :M] end
  def s3() [0, @height - @h, :W] end
  def s4() [@width - @w, @height - @h, :W] end
end

class Five < Four
  alias :s5 :s4
  alias :s4 :s3
  define_method :s3, Ace.instance_method(:s1)
end

class Six < Four
  alias :s6 :s3
  alias :s5 :s4
  def s3() [0, @height / 2 - @h / 2, :M] end
  def s4() [@width - @w, @height / 2 - @h / 2, :M] end
end

class Seven < Six
  alias :s7 :s6
  alias :s6 :s5
  alias :s5 :s4
  alias :s4 :s3
  def s3() [@width / 2 - @w / 2, @height / 4 - @h / 2, :M] end
end

class Eight < Seven
  alias :s8 :s7
  alias :s7 :s6
  def s6() [@width / 2 - @w / 2, @height / 4 * 3 - @h / 2, :W] end
end

class Nine < Four
  alias :s9 :s4
  alias :s8 :s3
  define_method :s5, Ace.instance_method(:s1)
  def s3() [0, (@height - @h) / 3, :M] end
  def s4() [@width - @w, (@height - @h) / 3, :M] end
  def s6() [0, (@height - @h) / 3 * 2 , :W] end
  def s7() [@width - @w, (@height - @h) / 3 * 2, :W] end
end

class Ten < Nine
  alias :s10 :s9
  alias :s9 :s8
  def s8() [@width / 2 - @w / 2, (@height - @h) / 6 * 5, :W] end
  # does not work due to inheritance gap
  # define_method :s8, Eight.instance_method(:s6)
  alias :s5 :s4
  alias :s4 :s3
  def s3() [@width / 2 - @w / 2, (@height - @h) / 6, :M] end
  # define_method :s3, Seven.instance_method(:s3)
end

number = $*[0]; params = $*[1].nil? ? {} : eval($*[1]);
params[:x] ||= 0; params[:y] ||= 0

puts ({
  "A" => Ace, "2" => Two, "3" => Three, "4" => Four, "5" => Five,
  "6" => Six, "7" => Seven, "8" => Eight, "9" => Nine, "10" => Ten
})[number].new(params).collect { |x| 
  [x[0] + params[:x], x[1] + params[:y], x[2]]
}.inspect.gsub(":W", "True").gsub(":M", "False")
