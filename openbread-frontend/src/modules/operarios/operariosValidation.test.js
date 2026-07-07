import test from 'node:test';
import assert from 'node:assert/strict';
import { getPasswordHelper, getNifHelper, getEmailHelper, getNameHelper } from './operariosValidation.js';

test('getPasswordHelper reports missing requirements clearly', () => {
  assert.equal(getPasswordHelper('abc'), 'Falta: mín. 6 caracteres, una mayúscula, un número, un carácter especial');
});

test('getPasswordHelper returns success message when password is strong', () => {
  assert.equal(getPasswordHelper('Strong1!'), '¡Contraseña segura!');
});

test('getNifHelper returns empty in edit mode', () => {
  assert.equal(getNifHelper('12345678Z', true), '');
});

test('getEmailHelper returns helpful hint for invalid email', () => {
  assert.equal(getEmailHelper('invalid'), 'Ejemplo: usuario@dominio.com');
});

test('getNameHelper rejects numeric values', () => {
  assert.equal(getNameHelper('Ana2'), 'No se permiten números');
});
