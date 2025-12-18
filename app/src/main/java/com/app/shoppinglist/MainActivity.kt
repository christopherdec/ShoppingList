package com.app.shoppinglist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.shoppinglist.data.dataStore
import com.app.shoppinglist.data.model.ShoppingItem
import com.app.shoppinglist.data.repository.ShoppingListRepositoryImpl
import com.app.shoppinglist.ui.theme.Coral
import com.app.shoppinglist.ui.theme.Navy
import com.app.shoppinglist.ui.theme.ShoppingListTheme
import com.app.shoppinglist.ui.theme.Typography
import java.text.DateFormat
import java.util.Date

class MainActivity : ComponentActivity() {

    private val repository by lazy {
        ShoppingListRepositoryImpl(dataStore)
    }

    private val viewModel by viewModels<ShoppingListViewModel> {
        ShoppingListViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShoppingListTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ShoppingList(modifier = Modifier.padding(innerPadding), viewModel)
                }
            }
        }
    }
}

@Composable
fun ShoppingList(modifier: Modifier = Modifier, viewModel: ShoppingListViewModel) {

    val itemList by viewModel.itemList.collectAsState()

    LazyColumn(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        item {
            TopImage()
            AddItem(
                onAddItem = viewModel::add
            )
            Spacer(
                modifier = Modifier.height(48.dp)
            )
            Title(
                text = "Shopping List",
            )
            Spacer(
                modifier = Modifier.height(24.dp)
            )
        }
        shoppingItemList(
            list = itemList.filter { !it.onCart },
            onCheck = viewModel::toggleOnCart,
            onRemove = viewModel::remove,
            onEdit = viewModel::rename
        )
        if (itemList.isEmpty()) {
            item {
                Text(
                    text = "Your list is empty. Add items to it using the form above!",
                    style = Typography.bodyLarge
                )
            }
        } else {
            item {
                Spacer(
                    modifier = Modifier.height(40.dp)
                )
                Title(
                    text = "On Cart",
                )
                Spacer(
                    modifier = Modifier.height(24.dp)
                )
            }
            shoppingItemList(
                itemList.filter { it.onCart },
                onCheck = viewModel::toggleOnCart,
                onRemove = viewModel::remove,
                onEdit = viewModel::rename
            )
        }
    }
}

fun LazyListScope.shoppingItemList(
    list: List<ShoppingItem>,
    onCheck: (item: ShoppingItem) -> Unit = {},
    onRemove: (item: ShoppingItem) -> Unit = {},
    onEdit: (item: ShoppingItem, name: String) -> Unit = { _, _ -> },
) {
    items(list.size) { index ->
        ListItem(
            item = list[index],
            onCheck = onCheck,
            onRemove = onRemove,
            onEdit = onEdit
        )
    }
}

@Composable
fun Title(text: String, modifier: Modifier = Modifier) {
    Text(
        text,
        style = Typography.headlineLarge,
        modifier = modifier
            .padding(bottom = 8.dp)
            .fillMaxWidth(),
        textAlign = TextAlign.Left
    )
    DottedLine(modifier)
}

@Composable
fun DottedLine(modifier: Modifier = Modifier) {
    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 5f), 2.5f)
    Canvas(modifier = modifier.fillMaxWidth()) {
        drawLine(
            color = Coral,
            pathEffect = pathEffect,
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
            strokeWidth = 4f
        )
    }
}

@Composable
fun TopImage(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.grocery),
        contentDescription = null,
        modifier = modifier.size(160.dp)
    )
}

@Composable
fun StyledIcon(imageVector: ImageVector, modifier: Modifier = Modifier) {
    Icon(
        imageVector = imageVector,
        contentDescription = "Icon",
        modifier = modifier,
        tint = Navy
    )
}

@Composable
fun ListItem(
    item: ShoppingItem,
    modifier: Modifier = Modifier,
    onCheck: (item: ShoppingItem) -> Unit = {},
    onRemove: (item: ShoppingItem) -> Unit = {},
    onEdit: (item: ShoppingItem, name: String) -> Unit = { _, _ -> },
) {
    Column(
        verticalArrangement = Arrangement.Top,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            var editText by rememberSaveable { mutableStateOf(item.name) }

            var isEditing by rememberSaveable { mutableStateOf(false) }

            Checkbox(
                item.onCart,
                onCheckedChange = {
                    onCheck(item)
                },
                modifier = Modifier
                    .padding(end = 8.dp)
                    .requiredSize(24.dp)
            )
            if (isEditing) {
                OutlinedTextField(
                    value = editText,
                    onValueChange = {
                        editText = it
                    },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    shape = RoundedCornerShape(24.dp)
                )
                IconButton(
                    onClick = {
                        onEdit(item, editText)
                        isEditing = false
                    }
                ) {
                    StyledIcon(
                        Icons.Default.Done, modifier = Modifier
                            .size(16.dp)
                    )
                }
            } else {
                Text(
                    item.name,
                    modifier = Modifier.weight(1f),
                    style = Typography.bodyMedium,
                    textAlign = TextAlign.Start
                )
            }
            IconButton(
                onClick = {
                    onRemove(item)
                },
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(16.dp)
            ) {
                StyledIcon(
                    Icons.Default.Delete
                )
            }
            IconButton(
                onClick = {
                    isEditing = true
                },
                modifier = Modifier.size(16.dp)
            ) {
                StyledIcon(
                    Icons.Default.Edit
                )
            }
        }
        Text(
            text = formatDate(item.date),
            modifier = Modifier.padding(top = 8.dp),
            style = Typography.labelSmall
        )
    }
}

@Composable
fun AddItem(onAddItem: (item: ShoppingItem) -> Unit, modifier: Modifier = Modifier) {
    val text = rememberSaveable { mutableStateOf("") }

    OutlinedTextField(
        value = text.value,
        onValueChange = {
            text.value = it
        },
        placeholder = {
            Text(
                text = "Type the item you want to add",
                color = Color.Gray,
                style = Typography.bodyMedium
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        singleLine = true,
        shape = RoundedCornerShape(24.dp)
    )
    Button(
        onClick = {
            if (text.value.isNotBlank()) {
                onAddItem(ShoppingItem(text.value.trim()))
                text.value = ""
            }
        },
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        contentPadding = PaddingValues(16.dp, 12.dp)
    ) {
        Text(
            "Add item",
            color = Color.White,
            style = Typography.bodyLarge
        )
    }
}

@Preview
@Composable
private fun TitlePreview() {
    ShoppingListTheme {
        Title("Title Preview")
    }
}

@Preview
@Composable
private fun TopImagePreview() {
    ShoppingListTheme {
        TopImage()
    }
}

@Preview
@Composable
private fun IconPreview() {
    ShoppingListTheme {
        StyledIcon(Icons.Default.Edit)
    }
}

@Preview
@Composable
private fun ListItemPreview() {
    ShoppingListTheme {
        ListItem(
            ShoppingItem("Label")
        )
    }
}

@Preview
@Composable
private fun AddItemPreview() {
    ShoppingListTheme {
        AddItem({})
    }
}

fun formatDate(date: Date): String {
    return DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT)
        .format(date)
}