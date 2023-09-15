/*
    1) Adicionar um ecrã inicial a explicar cores, etc. <- Artur
    2) Adicionar feedback de clique (som e/ou visual) <- Diogo
    4) Calcular os Fitts indexes
*/

// Bakeoff #2 - Seleção de Alvos Fora de Alcance
// IPM 2021-22, Período 3
// Entrega: até dia 22 de Abril às 23h59 através do Fenix
// Bake-off: durante os laboratórios da semana de 18 de Abril
// p5.js reference: https://p5js.org/reference/

// Database (CHANGE THESE!)
const GROUP_NUMBER   = "53-AL";      // Add your group number here as an integer (e.g., 2, 3)
const VERSION        = 5.0;
const BAKE_OFF_DAY   = true;  // Set to 'true' before sharing during the bake-off day

// Target and grid properties (DO NOT CHANGE!)
let PPI, PPCM;
let TARGET_SIZE;
let TARGET_PADDING, MARGIN, LEFT_PADDING, TOP_PADDING;
let continue_button;
let inputArea        = {x: 0, y: 0, h: 0, w: 0}    // Position and size of the user input area

// Input Area Virtual Positions
let v_left_pad_start;
let v_left_pad_stop;
let v_right_pad_start;
let v_right_pad_stop;

// Virtual cursor
let v_mouseX;
let v_mouseY;
let last_mouseX;
let last_mouseY;

// Metrics
let testStartTime, testEndTime;// time between the start and end of one attempt (54 trials)
let hits 			 = 0;      // number of successful selections
let misses 			 = 0;      // number of missed selections (used to calculate accuracy)
let database;                  // Firebase DB  

// Study control parameters
let draw_targets     = false;  // used to control what to show in draw()
let trials 			 = [];     // contains the order of targets that activate in the test
let current_trial    = 0;      // the current trial number (indexes into trials array above)
let attempt          = 0;      // users complete each test twice to account for practice (attemps 0 and 1)
let fitts_IDs        = [];     // add the Fitts ID for each selection here (-1 when there is a miss)

// Design elements
let col_active_target;
let col_next_target;
let col_dummy_target;
let col_selection;

// Target class (position and width)
class Target
{
  constructor(x, y, w)
  {
    this.x = x;
    this.y = y;
    this.w = w;
  }
}

// Runs once at the start
function setup()
{
  createCanvas(700, 500);    // window size in px before we go into fullScreen()
  frameRate(60);             // frame rate (DO NOT CHANGE!)
  
  randomizeTrials();         // randomize the trial order at the start of execution
  
  textFont("Arial", 18);     // font size for the majority of the text
  drawUserIDScreen();        // draws the user start-up screen (student ID and display size)
  // Set drawing variables
  col_active_target = color(0, 255, 0);
  col_next_target = color(150 ,25, 23);
  col_dummy_target = color(119, 119, 118);
  col_selection = color(46,164, 212);
  last_mouseX = -1;
  last_mouseY = -1;
}

// Runs every frame and redraws the screen
function draw()
{
  if (draw_targets)
  {     
    // The user is interacting with the 6x3 target grid
    background(color(0,0,0));        // sets background to black
    
    // Print trial count at the top left-corner of the canvas
    fill(color(255,255,255));
    textAlign(LEFT);
    text("Trial " + (current_trial + 1) + " of " + trials.length, 50, 20);
    
    // Draw all 18 targets
	for (var i = 0; i < 18; i++) drawTarget(i);
    let target = getTargetBounds(trials[current_trial]);
    let next_target = getTargetBounds(trials[current_trial + 1]);
    strokeWeight(4);
    stroke(0, 125, 0);
    line(target.x, target.y, next_target.x, next_target.y);
    stroke(255, 255, 255);
    
    // Draw the user input area
    drawInputArea();
    
    // Draw the virtual cursor
    let x = map(mouseX, inputArea.x, inputArea.x + inputArea.w, 0, width);
    let y = map(mouseY, inputArea.y, inputArea.y + inputArea.h, 0, height);
    
    drawGrid();
    strokeWeight(4);
    stroke(0, 255, 0);
    line(x, y, target.x, target.y);
    fill(color(255,255,255));
    circle(x, y, 0.5 * PPCM);
    strokeWeight(1);
    stroke(255, 255, 255);
    text("The visual ball is not the cursor. The virtual cursor snaps to targets.", 770, 100, 800, 200);
    text("The virtual cursor is represented by a black dot", 770, 150);
    getVirtualCoordinates();
    noStroke();
    fill(0, 0, 0);
    circle(v_mouseX, v_mouseY, 0.05*PPCM);
    stroke(255, 255, 255);
  }
}

function drawGrid() {
  stroke(70, 70, 70);
  strokeWeight(1);
  line(LEFT_PADDING + 2*TARGET_PADDING, 0, LEFT_PADDING + 2*TARGET_PADDING, width);
  line(LEFT_PADDING + 4*TARGET_PADDING, 0, LEFT_PADDING + 4*TARGET_PADDING, width);
  for(i = 1; i <= 6; ++i) {
    line(LEFT_PADDING, TOP_PADDING + 2*i*TARGET_PADDING, LEFT_PADDING + 6*TARGET_PADDING, TOP_PADDING + 2*i*TARGET_PADDING);
  }
  line(LEFT_PADDING, TOP_PADDING + 2*TARGET_PADDING, LEFT_PADDING + 6*TARGET_PADDING, TOP_PADDING + 2*TARGET_PADDING);
  
}

// Print and save results at the end of 54 trials
function printAndSavePerformance()
{
  // DO NOT CHANGE THESE! 
  let accuracy			= parseFloat(hits * 100) / parseFloat(hits + misses);
  let test_time         = (testEndTime - testStartTime) / 1000;
  let time_per_target   = nf((test_time) / parseFloat(hits + misses), 0, 3);
  let penalty           = constrain((((parseFloat(95) - (parseFloat(hits * 100) / parseFloat(hits + misses))) * 0.2)), 0, 100);
  let target_w_penalty	= nf(((test_time) / parseFloat(hits + misses) + penalty), 0, 3);
  let timestamp         = day() + "/" + month() + "/" + year() + "  " + hour() + ":" + minute() + ":" + second();
  
  background(color(0,0,0));   // clears screen
  fill(color(255,255,255));   // set text fill color to white
  text(timestamp, 10, 20);    // display time on screen (top-left corner)
  
  textAlign(CENTER);
  text("Attempt " + (attempt + 1) + " out of 2 completed!", width/2, 60); 
  text("Hits: " + hits, width/2, 100);
  text("Misses: " + misses, width/2, 120);
  text("Accuracy: " + accuracy + "%", width/2, 140);
  text("Total time taken: " + test_time + "s", width/2, 160);
  text("Average time per target: " + time_per_target + "s", width/2, 180);
  text("Average time for each target (+ penalty): " + target_w_penalty + "s", width/2, 220);
  
  // Print Fitts IDS (one per target, -1 if failed selection, optional)
  // 
  print(fitts_IDs);
  text("Fitts Index of Performance", width/2, 290);
  for(i = 0; i < trials.length; ++i) {
    let printID = fitts_IDs[i];
    if(printID == -2) 
      printID = "---"
    else if(printID == -1)
      printID = "MISSED";
    if (i < (trials.length + 1) / 2)
      text("Trial " + (i + 1) + ": " + printID, width/4, 120 + 20*i);
    else
      text("Trial " + (i + 1) + ": " + printID, 3*width/4, 120 + 20*(i - 27));
  }
  
  // Saves results (DO NOT CHANGE!)
  let attempt_data = 
  {
        project_from:       GROUP_NUMBER,
        assessed_by:        student_ID,
        test_completed_by:  timestamp,
        attempt:            attempt,
        hits:               hits,
        misses:             misses,
        accuracy:           accuracy,
        attempt_duration:   test_time,
        time_per_target:    time_per_target,
        target_w_penalty:   target_w_penalty,
        fitts_IDs:          fitts_IDs
  }
  
  // Send data to DB (DO NOT CHANGE!)
  if (BAKE_OFF_DAY)
  {
    // Access the Firebase DB
    if (attempt === 0)
    {
      firebase.initializeApp(firebaseConfig);
      database = firebase.database();
    }
    
    // Add user performance results
    let db_ref = database.ref('G' + GROUP_NUMBER);
    db_ref.push(attempt_data);
  }
}

// Mouse button was pressed - lets test to see if hit was in the correct target
function mousePressed() 
{
  // Only look for mouse releases during the actual test
  // (i.e., during target selections)
  if (draw_targets)
  {
    // Get the location and size of the target the user should be trying to select
    let target = getTargetBounds(trials[current_trial]);
    
    // Check to see if the virtual cursor is inside the target bounds,
    // increasing either the 'hits' or 'misses' counters
        
    if (insideInputArea(mouseX, mouseY))
    {
      getVirtualCoordinates()
      
      if (dist(target.x, target.y, v_mouseX, v_mouseY) < target.w/2) {
        hits++;
        if(current_trial < trials.length)
          fitts_IDs[current_trial] = log(dist(last_mouseX, last_mouseY, target.x, target.y)/target.w + 1);
      } else {
        misses++;
        if(current_trial < trials.length)
          fitts_IDs[current_trial] = -1;
      }
      last_mouseX = v_mouseX;
      last_mouseY = v_mouseY;
      current_trial++;                 // Move on to the next trial/target      
    }

    // Check if the user has completed all 54 trials
    if (current_trial === trials.length)
    {
      fitts_IDs[0] = -2;
      testEndTime = millis();
      draw_targets = false;          // Stop showing targets and the user performance results
      printAndSavePerformance();     // Print the user's results on-screen and send these to the DB
      attempt++;                      
      
      // If there's an attempt to go create a button to start this
      if (attempt < 2)
      {
        continue_button = createButton('START 2ND ATTEMPT');
        continue_button.mouseReleased(continueTest);
        continue_button.position(width/2 - continue_button.size().width/2, 250 - continue_button.size().height/2);
      }
    }
    else if (current_trial === 1) testStartTime = millis();
  }
}

// Draw target on-screen
function drawTarget(i)
{
  // Get the location and size for target (i)
  let target = getTargetBounds(i);           

  // Check whether this target is the target the user should be trying to select
  if (trials[current_trial] === i) 
  { 
    getVirtualCoordinates();
    if(v_mouseX == target.x && v_mouseY == target.y)
    {
      fill(color(0,125, 0));
    }
    else {
      fill(col_active_target);
    }
    // Highlights the target the user should be trying to select
    // with a white border
    stroke(color(255, 255, 255));
    circle(target.x, target.y, target.w);
    
    if (trials[current_trial + 1] === i) {
      fill(col_next_target);
      circle(target.x, target.y, 0.7 * target.w);
    }
    // Remember you are allowed to access targets (i-1) and (i+1)
    // if this is the target the user should be trying to select
    //
  }
  else if(trials[current_trial + 1] === i) {
    fill(col_next_target);
    circle(target.x, target.y, target.w);
  }
  // Draws the other targets with a different color
  else {          
  // Draws the target
    fill(col_dummy_target);
    circle(target.x, target.y, target.w);
  }
}

// Returns the location and size of a given target
function getTargetBounds(i)
{
  var x = parseInt(LEFT_PADDING) + parseInt((i % 3) * (TARGET_SIZE + TARGET_PADDING) + MARGIN);
  var y = parseInt(TOP_PADDING) + parseInt(Math.floor(i / 3) * (TARGET_SIZE + TARGET_PADDING) + MARGIN);

  return new Target(x, y, TARGET_SIZE);
}

// Evoked after the user starts its second (and last) attempt
function continueTest()
{
  // Re-randomize the trial order
  shuffle(trials, true);
  current_trial = 0;
  print("trial order: " + trials);
  
  // Resets performance variables
  hits = 0;
  misses = 0;
  fitts_IDs = [];
  
  continue_button.remove();
  
  // Shows the targets again
  draw_targets = true;
  testStartTime = millis();  
}

// Is invoked when the canvas is resized (e.g., when we go fullscreen)
function windowResized() 
{
  resizeCanvas(windowWidth, windowHeight);
    
  let display    = new Display({ diagonal: display_size }, window.screen);

  // DO NOT CHANGE THESE!
  PPI            = display.ppi;                        // calculates pixels per inch
  PPCM           = PPI / 2.54;                         // calculates pixels per cm
  TARGET_SIZE    = 1.5 * PPCM;                         // sets the target size in cm, i.e, 1.5cm
  TARGET_PADDING = 1.5 * PPCM;                         // sets the padding around the targets in cm
  MARGIN         = 1.5 * PPCM;                         // sets the margin around the targets in cm

  // Sets the margin of the grid of targets to the left of the canvas (DO NOT CHANGE!)
  LEFT_PADDING   = width/3 - TARGET_SIZE - 1.5 * TARGET_PADDING - 1.5 * MARGIN;        

  // Sets the margin of the grid of targets to the top of the canvas (DO NOT CHANGE!)
  TOP_PADDING    = height/2 - TARGET_SIZE - 3.5 * TARGET_PADDING - 1.5 * MARGIN;
  
  // Defines the user input area (DO NOT CHANGE!)
  inputArea      = {x: width/2 + 2 * TARGET_SIZE,
                    y: height/2,
                    w: width/3,
                    h: height/3
                   }
  
  // Paddings translated to the input area
  v_left_pad_start = inputArea.x;
  v_left_pad_stop = inputArea.x + (LEFT_PADDING/width) * inputArea.w;
  v_right_pad_start = inputArea.x + (LEFT_PADDING/width + 6 * TARGET_PADDING/width) * inputArea.w;
  v_right_pad_stop = inputArea.x + inputArea.w;
  
  // Starts drawing targets immediately after we go fullscreen
  draw_targets = true;
}

// Responsible for drawing the input area
function drawInputArea()
{
  noFill();
  stroke(color(220,220,220));
  strokeWeight(2);
  
  rect(inputArea.x, inputArea.y, inputArea.w, inputArea.h);
}

// 18 targets, grid 3x6
function getVirtualCoordinates()
{
  // Dentro da zona de snapping
  let target;
  
  // 1st Target
  if(mouseX < v_left_pad_stop + 1/3 * (v_right_pad_start - v_left_pad_stop) && 
     mouseY >= inputArea.y && 
     mouseY < inputArea.y + 1/6 * inputArea.h)
  {
     target = getTargetBounds(0);
     v_mouseX = target.x; v_mouseY = target.y;
  }
  
  // 2nd Target
  else if(mouseX >= v_left_pad_stop + 1/3 * (v_right_pad_start - v_left_pad_stop) && 
     mouseX < v_left_pad_stop + 2/3 * (v_right_pad_start - v_left_pad_stop) && 
     mouseY > inputArea.y && 
     mouseY < inputArea.y + 1/6 * inputArea.h)
  {
     target = getTargetBounds(1);
     v_mouseX = target.x; v_mouseY = target.y;
  }
  
  // 3rd Target
  else if(mouseX >= v_left_pad_stop + 2/3 * (v_right_pad_start - v_left_pad_stop) && 
     mouseY >= inputArea.y && 
     mouseY < inputArea.y + 1/6 * inputArea.h)
  {
     target = getTargetBounds(2);
     v_mouseX = target.x; v_mouseY = target.y;
  }
  
  // 4th Target
  else if(mouseX < v_left_pad_stop + 1/3 * (v_right_pad_start - v_left_pad_stop) && 
     mouseY > inputArea.y + 1/6 * inputArea.h && 
     mouseY < inputArea.y + 2/6 * inputArea.h)
  {
     target = getTargetBounds(3);
     v_mouseX = target.x; v_mouseY = target.y;
  }
  
  // 5th Target
  else if(mouseX >= v_left_pad_stop + 1/3 * (v_right_pad_start - v_left_pad_stop)&& 
     mouseX < v_left_pad_stop + 2/3 * (v_right_pad_start - v_left_pad_stop) && 
     mouseY >= inputArea.y + 1/6 * inputArea.h && 
     mouseY < inputArea.y + 2/6 * inputArea.h)
  {
     target = getTargetBounds(4);
     v_mouseX = target.x; v_mouseY = target.y;
  }
  
  // 6th Target
  else if(mouseX >= v_left_pad_stop + 2/3 * (v_right_pad_start - v_left_pad_stop) &&
     mouseY >= inputArea.y + 1/6 * inputArea.h && 
     mouseY < inputArea.y + 2/6 * inputArea.h)
  {
     target = getTargetBounds(5);
     v_mouseX = target.x; v_mouseY = target.y;
  }
  
  // 7th Target
  else if(mouseX < v_left_pad_stop + 1/3 * (v_right_pad_start - v_left_pad_stop) && 
     mouseY >= inputArea.y + 2/6 * inputArea.h && 
     mouseY < inputArea.y + 3/6 * inputArea.h)
  {
     target = getTargetBounds(6);
     v_mouseX = target.x; v_mouseY = target.y;
  }
  
  // 8th Target
  else if(mouseX >= v_left_pad_stop + 1/3 * (v_right_pad_start - v_left_pad_stop)&& 
     mouseX < v_left_pad_stop + 2/3 * (v_right_pad_start - v_left_pad_stop) && 
     mouseY >= inputArea.y + 2/6 * inputArea.h && 
     mouseY < inputArea.y + 3/6 * inputArea.h)
  {
     target = getTargetBounds(7);
     v_mouseX = target.x; v_mouseY = target.y;
  }
  
  // 9th Target
  else if(mouseX >= v_left_pad_stop + 2/3 * (v_right_pad_start - v_left_pad_stop) &&
     mouseY >= inputArea.y + 2/6 * inputArea.h && 
     mouseY < inputArea.y + 3/6 * inputArea.h)
  {
     target = getTargetBounds(8);
     v_mouseX = target.x; v_mouseY = target.y;
  }

  // 10th Target
  else if(mouseX < v_left_pad_stop + 1/3 * (v_right_pad_start - v_left_pad_stop) && 
     mouseY >= inputArea.y + 3/6 * inputArea.h && 
     mouseY < inputArea.y + 4/6 * inputArea.h)
  {
     target = getTargetBounds(9);
     v_mouseX = target.x; v_mouseY = target.y;
  }
  
  // 11th Target
  else if(mouseX >= v_left_pad_stop + 1/3 * (v_right_pad_start - v_left_pad_stop)&& 
     mouseX < v_left_pad_stop + 2/3 * (v_right_pad_start - v_left_pad_stop) && 
     mouseY >= inputArea.y + 3/6 * inputArea.h && 
     mouseY < inputArea.y + 4/6 * inputArea.h)
  {
     target = getTargetBounds(10);
     v_mouseX = target.x; v_mouseY = target.y;
  }
  
  // 12th Target
  else if(mouseX >= v_left_pad_stop + 2/3 * (v_right_pad_start - v_left_pad_stop) &&
     mouseY >= inputArea.y + 3/6 * inputArea.h && 
     mouseY < inputArea.y + 4/6 * inputArea.h)
  {
     target = getTargetBounds(11);
     v_mouseX = target.x; v_mouseY = target.y;
  }
  
  // 13th Target
  else if(mouseX < v_left_pad_stop + 1/3 * (v_right_pad_start - v_left_pad_stop) && 
     mouseY >= inputArea.y + 4/6 * inputArea.h && 
     mouseY < inputArea.y + 5/6 * inputArea.h)
  {
     target = getTargetBounds(12);
     v_mouseX = target.x; v_mouseY = target.y;
  }
  
  // 14th Target
  else if(mouseX >= v_left_pad_stop + 1/3 * (v_right_pad_start - v_left_pad_stop)&& 
     mouseX < v_left_pad_stop + 2/3 * (v_right_pad_start - v_left_pad_stop) && 
     mouseY >= inputArea.y + 4/6 * inputArea.h && 
     mouseY < inputArea.y + 5/6 * inputArea.h)
  {
     target = getTargetBounds(13);
     v_mouseX = target.x; v_mouseY = target.y;
  }
  
  // 15th Target
  else if(mouseX >= v_left_pad_stop + 2/3 * (v_right_pad_start - v_left_pad_stop) &&
     mouseY >= inputArea.y + 4/6 * inputArea.h && 
     mouseY < inputArea.y + 5/6 * inputArea.h)
  {
     target = getTargetBounds(14);
     v_mouseX = target.x; v_mouseY = target.y;
  }
  
  // 16th Target
  else if(mouseX < v_left_pad_stop + 1/3 * (v_right_pad_start - v_left_pad_stop) && 
     mouseY >= inputArea.y + 5/6 * inputArea.h && 
     mouseY < inputArea.y + inputArea.h)
  {
     target = getTargetBounds(15);
     v_mouseX = target.x; v_mouseY = target.y;
  }
  
  // 17th Target
  else if(mouseX >= v_left_pad_stop + 1/3 * (v_right_pad_start - v_left_pad_stop)&& 
     mouseX < v_left_pad_stop + 2/3 * (v_right_pad_start - v_left_pad_stop) && 
     mouseY >= inputArea.y + 5/6 * inputArea.h && 
     mouseY < inputArea.y + inputArea.h)
  {
     target = getTargetBounds(16);
     v_mouseX = target.x; v_mouseY = target.y;
  }
  
  // 18th Target
  else if(mouseX >= v_left_pad_stop + 2/3 * (v_right_pad_start - v_left_pad_stop) &&
     mouseY >= inputArea.y + 5/6 * inputArea.h && 
     mouseY < inputArea.y + inputArea.h)
  {
     target = getTargetBounds(17);
     v_mouseX = target.x; v_mouseY = target.y;
  }