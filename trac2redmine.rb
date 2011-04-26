#!/usr/bin/ruby -w

# Created by Michael Williams 12/19/2005
# Licensed under Create Commons Attribution License

# Example 1 - Read File and close
text = ""
file = File.new("target/trac.wiki", "r")
while (line = file.gets)
	# puts "#{counter}: #{line}"
	text = text + line
end
file.close


# Titles
text = text.gsub(/^(\=+)\s(.+)\s(\=+)/) {|s| "\nh#{$1.length}. #{$2}\n"}
# External Links
text = text.gsub(/\[(http[^\s]+)\s+([^\]]+)\]/) {|s| "\"#{$2}\":#{$1}"}
# Ticket links:
#      [ticket:234 Text],[ticket:234 This is a test]
text = text.gsub(/\[ticket\:([^\ ]+)\ (.+?)\]/, '"\2":/issues/show/\1')
#      ticket:1234
#      #1 is working cause Redmine uses the same syntax.
text = text.gsub(/ticket\:([^\ ]+)/, '#\1')
# Milestone links:
#      [milestone:"0.1.0 Mercury" Milestone 0.1.0 (Mercury)]
#      The text "Milestone 0.1.0 (Mercury)" is not converted,
#      cause Redmine's wiki does not support this.
text = text.gsub(/\[milestone\:\"([^\"]+)\"\ (.+?)\]/, 'version:"\1"')
#      [milestone:"0.1.0 Mercury"]
text = text.gsub(/\[milestone\:\"([^\"]+)\"\]/, 'version:"\1"')
text = text.gsub(/milestone\:\"([^\"]+)\"/, 'version:"\1"')
#      milestone:0.1.0
text = text.gsub(/\[milestone\:([^\ ]+)\]/, 'version:\1')
text = text.gsub(/milestone\:([^\ ]+)/, 'version:\1')
# Internal Links
text = text.gsub(/\[\[BR\]\]/, "\n") # This has to go before the rules below
text = text.gsub(/\[\"(.+)\".*\]/) {|s| "[[#{$1.delete(',./?;|:')}]]"}
text = text.gsub(/\[wiki:\"(.+)\".*\]/) {|s| "[[#{$1.delete(',./?;|:')}]]"}
text = text.gsub(/\[wiki:\"(.+)\".*\]/) {|s| "[[#{$1.delete(',./?;|:')}]]"}
text = text.gsub(/\[wiki:([^\s\]]+)\]/) {|s| "[[#{$1.delete(',./?;|:')}]]"}
text = text.gsub(/\[wiki:([^\s\]]+)\s(.*)\]/) {|s| "[[#{$1.delete(',./?;|:')}|#{$2.delete(',./?;|:')}]]"}

# Links to pages UsingJustWikiCaps
text = text.gsub(/([^!]|^)(^| )([A-Z][a-z]+[A-Z][a-zA-Z]+)/, '\\1\\2[[\3]]')
# Normalize things that were supposed to not be links
# like !NotALink
text = text.gsub(/(^| )!([A-Z][A-Za-z]+)/, '\1\2')
# Revisions links
text = text.gsub(/\[(\d+)\]/, 'r\1')

# Ticket number re-writing
#text = text.gsub(/#(\d+)/) do |s|
#  if $1.length < 10
#            TICKET_MAP[$1.to_i] ||= $1
#    "\##{TICKET_MAP[$1.to_i] || $1}"
#  else
#    s
#  end
#end

# We would like to convert the Code highlighting too
# This will go into the next line.
shebang_line = false
# Reguar expression for start of code
pre_re = /\{\{\{/
# Code hightlighing...
shebang_re = /^\#\!([a-z]+)/
# Regular expression for end of code
pre_end_re = /\}\}\}/

# Go through the whole text..extract it line by line
text = text.gsub(/^(.*)$/) do |line|
  m_pre = pre_re.match(line)
  if m_pre
    line = '<pre>'
  else
    m_sl = shebang_re.match(line)
    if m_sl
      shebang_line = true
      line = '<code class="' + m_sl[1] + '">'
    end
    m_pre_end = pre_end_re.match(line)
    if m_pre_end
      line = '</pre>'
      if shebang_line
        line = '</code>' + line
      end
    end
  end
  line
end

# Highlighting
text = text.gsub(/'''''([^\s])/, '_*\1')
text = text.gsub(/([^\s])'''''/, '\1*_')
text = text.gsub(/'''/, '*')
text = text.gsub(/''/, '_')
text = text.gsub(/__/, '+')
text = text.gsub(/~~/, '-')
text = text.gsub(/`/, '@')
text = text.gsub(/,,/, '~')
# Lists
text = text.gsub(/^([ ]+)\* /) {|s| '*' * $1.length + " "}

File.open('target/redmine.wiki', 'w') do |f|
  f.puts text
end


