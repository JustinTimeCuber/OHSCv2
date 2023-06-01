# OHSCv2
Scoreboard for the card game Oh Hell, made with Processing.

# Game Description

## Bidding
In Oh Hell, a number of cards is dealt to all players and a "trump" suit is chosen (generally from the top of the deck). Players then proceed to "bid" (from the left of the dealer) on how many "tricks" (rounds of each person playing one card) they will take throughout the hand. The dealer (who is the last bidder) is not allowed to make the total bid by all players equal the number of tricks available. That is, if 9 cards are dealt (9 tricks are available), and the total bid prior to the dealer is 7, the dealer must not bid 2, but may bid 0, 1, 3, etc.

## Playing
After everyone has recorded their bid, play begins starting with the person to the left of the dealer. Everyone must follow suit unless they do not have that suit, and the winner of the trick is whoever played the highest card of that suit (aces are high), or if the trump suit was played, whoever has the highest trump. Following suit also applies for trump, so if trump is led, then everyone who has it must play it. Play continues until all cards have been played, with the person who wins each trick starting (or "leading") the next one.

## Scoring
After a full hand is played, each player is scored based on their bid and their tricks taken. If they took too few tricks, they simply get 1 point for each trick taken; for example, if a player bids 3 and takes 2, they receive 2 points. If they took too many tricks, they lose a flat 10 points (negative scores are possible). This is regardless of how many extra tricks are taken, so a player who bids 1 and takes 4 would still lose 10 points (not 30). If a player exactly matches their bid, they gain one point for each trick plus a 10 point bonus. So, a player who bids 3 and takes 3 would get 13 points. Bidding zero is allowed; if a player bids 0 and takes 0, they would gain 10 points.

# Scoreboard Software Description
This scoreboard is written in Java using Processing as a library. In order to compile the program from source, you will need to obtain the Processing core.jar file and include it as a library. If you just want to run the program, you can run the published JAR file. Make sure that you have Java 17 or later installed on your computer and you are running Windows, Linux, or macOS. The same JAR file should work across those platforms.

There are two main screens within the program: setup mode, and scoreboard mode.

## Setup mode
![image](https://github.com/JustinTimeCuber/OHSCv2/assets/46458276/5377a9fc-981a-47b5-a7b0-f3d7722dc4f4)
Setup mode is used for entering players' names, setting the theme (color scheme), changing the settings, and manually altering scores.
There are two popup windows that can be accessed from setup mode:
![image](https://github.com/JustinTimeCuber/OHSCv2/assets/46458276/eaffa74f-c613-4efc-bdda-5186cfe11354)
This is the trick customization window, which is used to create the trick sequence, i.e. the numbers of cards that will be dealt on each hand. This is determined by the number of cards in the deck (usually 52, but can vary e.g. with additional suits) as well as the trick mode, which determines whether the sequence goes down then up, down only, up only, up then down, or split. Split means going down by 2 and then up by 2 such that all numbers are covered once, e.g. 7, 5, 3, 1, 2, 4, 6.
![image](https://github.com/JustinTimeCuber/OHSCv2/assets/46458276/91ee08bc-ee80-4391-a5c3-8aede567a243)
This is the theme selector window, which is used to select the color scheme of the scoreboard. There are four built-in themes plus an example custom theme. Additional custom themes can be added or removed by creating new files for them and adding them to themes/external_themes.txt, which is located in the OHSCv2 folder. This can be found in C:\Users\USERNAME\AppData\Roaming\ on Windows, /home/USERNAME/.local/share/ on Linux, and /Users/USERNAME/Library/Application Support/ on macOS.

## Scoreboard mode
![image](https://github.com/JustinTimeCuber/OHSCv2/assets/46458276/e9689d37-9f45-4ce9-b9b2-bcd98adc7ecf)
Scoreboard mode is used for showing the score while playing the game. It allows for entering bids and tricks taken, and it automatically calculates the score after each round. Bids can be increased by left-clicking and decreased by right-clicking, and the same applies for tricks taken. Note that all players must have a bid entered even if it is zero. To enter a zero bid, right-click the player. Certain checks can be overridden by holding enter and right-clicking the corresponding button.

## Contributing
OHSCv2 is free and open-source software; if you find a bug or have a feature request, please open an issue. Additionally, contributions through pull requests are welcome, but if you plan on writing a substantial amount of code, please ask if those contributions would be worthwhile/likely to be accepted to avoid potentially wasting time.
