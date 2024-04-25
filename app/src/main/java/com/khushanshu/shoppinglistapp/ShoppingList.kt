package com.khushanshu.shoppinglistapp

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp;
import androidx.compose.ui.unit.sp


//data class to store data of shopping list item
data class ShoppingItems(val id:Int,var name:String, var quantity:Int, var isBeingEdited:Boolean=false);

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListApp(){
    var sItems by remember{ mutableStateOf(listOf<ShoppingItems>()) };
    //now our list of items is modifiable as it is now a state which can be changed

    var showDialog by remember{ mutableStateOf(false) };
    //tells or stores the state of dialog that is it is opened or not

    var itemName by remember{ mutableStateOf("") };
    //it represents the name of item  that we will enter state as we can also edit it later

    var itemQuantity by remember{ mutableStateOf("") };
    //it represents the quantity of item that we will enter state as we can also edit it later

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center){


        Button( onClick = {showDialog=true}, modifier = Modifier.align(Alignment.CenterHorizontally) ){
            Text(text = "Add Item");
        }

        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp))
        {
            //fillMaxSize makes sure our lazy column takes entire space available PUSHING BUTTON AT TOP AUTOMATICALLY
            //padding is of lazy column not of its content as modifier modifies the composable only


            //BELOW BLOCK REPRESENTS THE ITEMS IN LAZY LIST WHICH IS A LIST OF "ShoppingItems" NAMED AS sItems
            items(sItems) {
                //each item in list will be in either editing state or not and if being edited we will show different composable and if not then other

                //item == it i.e current item in lazy column being shown i.e ShoppingItem

                //in if == if item is being edit compose the  ShoppingItemEditor
                //else display the  ShoppingListItem
                item->
                if(item.isBeingEdited){

                    ShoppingItemEditor(
                        item = item,
                        onEditComplete = {
                            editedName,editedQuantity->

                            sItems=sItems.map{it.copy(isBeingEdited = false)}  //set isBeingEdited false for each item in list by copy
                            val editedItem=sItems.find { it.id==item.id }      //finds the item we are editing from list by id (in "it" one by one values comes )

                            //unpacking the the founded item that has been edited
                            editedItem?.let{
                                it.name=editedName
                                it.quantity=editedQuantity
                            }
                        }
                    )

                }
                else{
                    ShoppingListItem(item = item,
                        onEditClick = {
                            //finding out which item in lazy column we have clicked for edit by matching ids
                            //i.e use map to traverse now it will have element now copy it with value of isBeingEdited set to true if if matches
                            sItems=sItems.map { it.copy(isBeingEdited = (it.id==item.id) ) }
                        },
                        onDeleteClick = {
                            //remove that from list
                            sItems-=item
                        }
                    )

                }

            }

        }

        if(showDialog){
            AlertDialog(
                onDismissRequest = { showDialog=false  },//put itemName="" amd itemQunatity="" if want to reset both on dismissal
                confirmButton = {
                                Row(modifier = Modifier
                                    .padding(8.dp)
                                    .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween)
                                {
                                    Button(onClick = {
                                        if(itemName.isNotBlank()){
                                            val newItem=ShoppingItems(id=sItems.size+1,name=itemName, quantity =itemQuantity.toInt());
                                            sItems+=newItem;
                                            showDialog=false; //As item is added close the box
                                            itemName=""; //resetting the value as of input text to empty again
                                            itemQuantity="";
                                        }
                                    } ) {
                                        Text(text = "Add")

                                    }

                                    Button(onClick = {
                                        itemName="";
                                        itemQuantity="";
                                        showDialog=false;
                                    }) {
                                        Text(text = "Cancel")

                                    }

                                }
                },
                title ={Text("Add Shopping Item",color= Color.White)},
                text = {
                    Column {
                        OutlinedTextField(
                            value =itemName ,
                            onValueChange={itemName=it},
                            modifier= Modifier
                                .fillMaxWidth()
                                .padding(8.dp)

                        )

                        OutlinedTextField(
                            value =itemQuantity,
                            onValueChange={itemQuantity=it},
                            modifier= Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Number // Set keyboard type to numeric
                            )

                        )
                    }
                }

            )
        }

    }
}


@Composable
fun ShoppingItemEditor(item:ShoppingItems,onEditComplete:(String,Int)->Unit){
    var editedName by remember{ mutableStateOf(item.name) }
    var editedQuantity by remember{ mutableStateOf(item.quantity.toString()) }
    var isEditing by remember { mutableStateOf(item.isBeingEdited) } //true if item is being edited
    Row(modifier= Modifier
        .padding(8.dp)
        .fillMaxWidth()
        .border(border = BorderStroke(2.dp, Color.White), shape = RoundedCornerShape(20))
        //.background(Color.White)
        , horizontalArrangement = Arrangement.SpaceEvenly )
    {

        Column {
            BasicTextField(value = editedName, onValueChange = {editedName=it}, singleLine=true, modifier= Modifier
                .wrapContentSize()
                .padding(8.dp), textStyle = LocalTextStyle.current.copy(color = Color.White,fontSize = 16.sp) )
            BasicTextField(value = editedQuantity, onValueChange = {editedQuantity=it}, singleLine=true, modifier= Modifier
                .wrapContentSize()
                .padding(8.dp),textStyle = LocalTextStyle.current.copy(color = Color.White,fontSize = 16.sp) )

        }

        Column (verticalArrangement = Arrangement.Center){
            Button(
                modifier = Modifier.padding(16.dp),
                onClick = {
                    isEditing=false;
                    onEditComplete(editedName,editedQuantity.toIntOrNull()?:1);  //set to one if quantity entered is not a int after conversion

                })
            {
                Text("Save")

            }
        }





    }
}

@Composable
//A COMPOSABLE FUNCTION WHICH REPRESENTS THE ITEM IN LIST OR LAZY LIST
//onEditClick is of lambda function type i.e it will be executed whenever ShoppingListItem will be called as per the composable or function body
//passed during calling time which is done as {BODY} SO WE CREATE OUR OWN COMPOSABLE LIKE onClick
fun ShoppingListItem(item:ShoppingItems, onEditClick:()->Unit, onDeleteClick:()->Unit){
    Row(modifier= Modifier
        .padding(16.dp)
        .fillMaxWidth()
        .border(border = BorderStroke(2.dp, Color.White), shape = RoundedCornerShape(20)), horizontalArrangement = Arrangement.SpaceBetween)
    {
        Text(text=item.name,modifier=Modifier.padding(16.dp), color = Color.White)
        Text(text="Quantity: ${item.quantity}",modifier=Modifier.padding(16.dp), color = Color.White)

        Row {
            //ICON BUTTON CREATES A BUTTON WITH ICON
            IconButton(onClick =onEditClick) {
                Icon(imageVector = Icons.Default.Edit , contentDescription =null)
            }
            IconButton(onClick =onDeleteClick) {
                Icon(imageVector = Icons.Default.Delete , contentDescription =null)
            }
        }
    }
}