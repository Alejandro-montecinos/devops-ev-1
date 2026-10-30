package com.duocuc.pedidosBackend.service;

import com.duocuc.pedidosBackend.model.entities.Productos360;
import com.duocuc.pedidosBackend.model.request.AgregarProducto;
import com.duocuc.pedidosBackend.model.request.ActualizarMarca;
import com.duocuc.pedidosBackend.repositories.ProductosRepositories;
import com.duocuc.pedidosBackend.services.ProductosService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductosServiceTest {

    @Mock
    private ProductosRepositories productosRepositories;

    @InjectMocks
    private ProductosService productosService;

    private Productos360 producto;

    @BeforeEach
    void setUp() {
        producto = new Productos360();
        producto.setNombre("Laptop");
        producto.setDescripcion("Laptop gamer");
        producto.setPrecio(999.99f);
    }

    @Test
    void obtenerTodosLosProductos_retornaLista() {
        Productos360 p2 = new Productos360();
        p2.setNombre("Mouse");
        p2.setDescripcion("Mouse inalambrico");
        p2.setPrecio(29.99f);

        when(productosRepositories.findAll()).thenReturn(Arrays.asList(producto, p2));

        List<Productos360> resultado = productosService.obtenerTodosLosProductos();

        assertEquals(2, resultado.size());
        verify(productosRepositories, times(1)).findAll();
    }

    @Test
    void obtenerProductoPorId_existente_retornaProducto() {
        when(productosRepositories.findById(1)).thenReturn(Optional.of(producto));

        Productos360 resultado = productosService.obtenerProductosPorId(1);

        assertNotNull(resultado);
        assertEquals("Laptop", resultado.getNombre());
    }

    @Test
    void obtenerProductoPorId_noExistente_lanzaExcepcion() {
        when(productosRepositories.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () ->
            productosService.obtenerProductosPorId(99)
        );
    }

    @Test
    void agregarProducto_guardaYRetornaProducto() {
        AgregarProducto request = new AgregarProducto();
        request.setNombre("Teclado");
        request.setDescripcion("Teclado mecanico");
        request.setPrecio(79.99f);

        when(productosRepositories.save(any(Productos360.class))).thenReturn(producto);

        Productos360 resultado = productosService.agregarProducto(request);

        assertNotNull(resultado);
        verify(productosRepositories, times(1)).save(any(Productos360.class));
    }

    @Test
    void eliminarProducto_existente_retornaMensaje() {
        when(productosRepositories.existsById(1)).thenReturn(true);
        doNothing().when(productosRepositories).deleteById(1);

        String resultado = productosService.eliminarProducto(1);

        assertEquals("Producto eliminado", resultado);
        verify(productosRepositories, times(1)).deleteById(1);
    }

    @Test
    void eliminarProducto_noExistente_lanzaExcepcion() {
        when(productosRepositories.existsById(99)).thenReturn(false);

        assertThrows(ResponseStatusException.class, () ->
            productosService.eliminarProducto(99)
        );
    }

    @Test
    void actualizarProducto_existente_actualizaYRetorna() {
        ActualizarMarca request = new ActualizarMarca();
        request.setNombre("Laptop Pro");
        request.setDescripcion("Laptop profesional");
        request.setPrecio(1299.99f);

        when(productosRepositories.findById(1)).thenReturn(Optional.of(producto));
        when(productosRepositories.save(any(Productos360.class))).thenReturn(producto);

        Productos360 resultado = productosService.actualizarProducto(request, 1);

        assertNotNull(resultado);
        verify(productosRepositories, times(1)).save(any(Productos360.class));
    }

    @Test
    void actualizarProducto_noExistente_lanzaExcepcion() {
        ActualizarMarca request = new ActualizarMarca();
        request.setNombre("X");
        request.setDescripcion("X");
        request.setPrecio(1.0f);

        when(productosRepositories.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () ->
            productosService.actualizarProducto(request, 99)
        );
    }
}
