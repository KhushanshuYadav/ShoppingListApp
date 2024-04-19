package com.khushanshu.shoppinglistapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


//data class to store data of shopping list item
data class ShoppingItems(val id:Int,var name:String, var quantity:Int, var isBeingEdited:Boolean=false);

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListApp(){
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center){

        var sItems by remember{ mutableStateOf(listOf<ShoppingItems>()) }
        //now our list of items is modifiable as it is now a state which can be changed

        var showDialog by remember{ mutableStateOf(false) }
        //tells or stores the state of dialog that is it is opened or not

        var itemName by remember{ mutableStateOf("") }
        //it represents the name of item  that we will enter state as we can also edit it later

        var itemQuantity by remember{ mutableStateOf("") }
        //it represents the quantity of item that we will enter state as we can also edit it later

        Button( onClick = {showDialog=true}, modifier = Modifier.align(Alignment.CenterHorizontally) ){
            Text(text = "Add Item");
        }

        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)){
            //fillMaxSize makes sure our lazy column takes entire space available PUSHING BUTTON AT TOP AUTOMATICALLY
            //padding is of lazy column not of its content as modifier modifies the composable only

            items(sItems) {


            }

        }

        if(showDialog){
            AlertDialog(
                onDismissRequest = { showDialog=false },
                confirmButton = {
                                Row(modifier = Modifier.padding(8.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Button(onClick = {
                                        if(itemName.isNotBlank()){
                                            val newItem=ShoppingItems(id=sItems.size+1,name=itemName, quantity =itemQuantity.toInt());
                                            sItems+=newItem;
                                            showDialog=false; //As item is added close the box
                                            itemName="";      //resetting the value as of input text to empty again

                                        }
                                    }) {
                                        Text(text = "Add")

                                    }

                                    Button(onClick = {showDialog=false}) {
                                        Text(text = "Cancel")

                                    }

                                }
                },
                title ={Text("Add Shopping Item")},
                text = {
                    Column {
                        OutlinedTextField(
                            value =itemName ,
                            onValueChange={itemName=it},
                            modifier= Modifier
                                .padding(8.dp)
                                .fillMaxWidth()
                        )

                        OutlinedTextField(
                            value =itemQuantity,
                            onValueChange={itemQuantity=it},
                            modifier= Modifier
                                .padding(8.dp)
                                .fillMaxWidth()
                        )
                    }
                }

            )
        }

    }
}