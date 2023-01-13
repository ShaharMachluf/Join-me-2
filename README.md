![alt text](https://github.com/ShaharMachluf/Join-me-2/blob/master/logo/Join%20me.png) 

# Join-me

## Teammates ✨

<!-- ALL-CONTRIBUTORS-LIST:START - Do not remove or modify this section -->
<!-- prettier-ignore-start -->
<!-- markdownlint-disable -->
<table>
  <tr>
    <td align="center"><a href="https://github.com/ShaharMachluf"><br /><sub><b>Shahar Machluf</b></sub></a><br /> </td>
    <td align="center"><a href="https://github.com/RazElbaz"><br /><sub><b>Raz Elbaz</b></sub></a><br /> </td>
    <td align="center"><a href="https://github.com/chenshtynmetz"><br /><sub><b>Chen Shtynmetz</b></sub></a><br /> </td>
    <td align="center"><a href="https://github.com/tavorlevine"><br /><sub><b>Tavor Levine</b></sub></a><br /> </td>
  </tr>
</table>

## Link to the code of the server our app uses:
https://github.com/chenshtynmetz/JoinMeServer

## Introduction:
As part of a software engineering course, we conduct a development simulation, which includes most of the stages of the software life cycle, including experiencing teamwork and creating objects characteristic of each stage.


## Evaluation components and project content:
• An organized and orderly Git repository that includes the course products (documents), explanations and the code.

• Treatment of two types of users who receive services or work with the system in a different role/manner.

• Implementation of 2-3 central implementation processes that work from end to end (depending on the size of the group).

• Organization of central logic in an application server (Django/Flask, net.ASP, js.Node) that can be invented On a local computer or in the cloud - you can also use functions cloud Firebase or any infrastructure Serverless otherwise.

• Using any database (for example Firebase, MySQL, SQL MS, etc.).

• Regarding a display component (Android): implementation of at least 3 of the following: notifications, sensors, screen ,dialog, menu, broadcast, switching to another device component (camera/phone/messages, etc.)

• Regarding the display (Internet, Windows):) Use of templates from the MV* family

• The display must look professional and include human engineering as taught in the course (you can use the templates available online).

• The system must be designed (structural design) based on accepted templates (layers, microservices, clean code)


## The project's description:

The project aims to solve a social problem in which people avoid various and occasional activities due to a lack of people. First and foremost, our app was created out of a desire to connect different people, while creating new friendships and fulfilling a variety of needs. Any person who desires in his heart to perform any activity, whether it is a multi-participant game, prayer, social entertainment, etc., can open a group or alternatively join an existing group depending on the place and time that suits him.
 **"Two are better than one"** 

## Project goals:

• Providing a quick and accessible solution to people who need a certain amount of people to carry out an activity.

• Fulfilling social and personal needs.

• Connecting people and creating friendships.

## Who is the system intended for?
Youth and adults who want to connect with a group of people.

## System description:
1. The participant (regular/manager) needs to log in to the application - using a Google account.
2. The user has various options in the application:

     - Create a group

     - joining the group

     - Receiving a list of groups he participated in

     - Report untrusted users

3. The administrator has various options in the application:
     - Same options as a normal user
     
     - Removing untrusted users
     
     - Displaying statistics
     
     - View a history of all created groups


## The actual system structure:

**Decentralization** - many computers working together, it is possible to make a connection, and divide tasks.

**Tier-N** There are n platforms that run our software.
The software is engineered so that it is divided logically and physically into different sections: the data management, the logical part (data processing) and the display functions.
This separation into several machines ensures that the services are provided without sharing the resources and that these services are delivered in the best way.
In this way it is easier to manage them because when there is a problem in a certain section it does not affect the rest of the system and if there is a problem you can easily know which part is responsible for it.

## Technological mapping:

![alt text](https://github.com/ShaharMachluf/Join-me-2/blob/master/logo/Technological_mapping.png) 

## USE CASE DIAGRAM:

![alt text](https://github.com/ShaharMachluf/Join-me-2/blob/master/logo/diagram.png) 

