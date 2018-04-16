-----CREATE NETWORK-----
* Starts with Main
* NetworkBuilder creates a network with weights and biases (values in constants but somewhere between -1 and 1)

-----CREATE TRAINING SET-----
* Starts with MnistTrainSetTools
* Load every image
* Load every image label
* Initialise the input and output neurons with 0's
* -- For each image
* Set the output neuron with index equal to the images digit to 1 e.g. Image is a two = [0,0,1,0...]
* For each input neuron set it to 0-1 value corresponding to the pixel colour (0=white, 1=black)
* Add the input and actual output to the training set and move to next image

-----TRAIN THE NETWORK-----
* Starts with Main trainData
* Run through the dataset multiple times for increased accuracy ??Confirm??
* Go to Network train method
* Create a batch of randomly selected images from the full TrainSet

* -- For each image in batch
* ** calculate **
* Essentially the equation is Ej(Oj*Wij) + B , i=current neuron, j = neuron in previous layer
* Set the output of the input layer to the 0-1 pixel colour values
* -- -- For each layer starting from the first hidden layer
* -- --  -- For each neuron
* Sum the bias with the output of each neuron in the previous layer multiplied by the weight of the connection of this neuron to the neuron in the previous layer
* Set the output of this neuron equal to the above sum run through an activation function (sigmoid in this case)
* Run the output through the derivative of the activation function ??used for error??
* ** backpropError **
* -- For each neuron in the output layer
* Calculate the error signal for each neuron in the final layer which is the predicted value - actual value multiplied by the derivative
* -- Working Backwards For each layer excluding the output layer
* -- -- For every neuron in current layer
* -- -- -- For every neuron in next layer
* Sum all the error signal from the next layer with the weight multiplied by the weight between the two neurons
* The error signal is the sum of the above multiplied by the output derivative of the current neuron
* ** updateWeights **
* -- For each layer excluding the input layer
* -- -- For each neuron
* Calculate the delta which is -eta multiplied by the error signal
* Add that to the bias
* -- -- -- For each neuron in the previous layer add the delta multiplied to the output of the prev neuron to the weight.

-----Questions-----
* Why does the error signal include the output derivative?
* What is eta?